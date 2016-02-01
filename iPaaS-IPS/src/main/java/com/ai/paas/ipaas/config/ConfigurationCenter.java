package com.ai.paas.ipaas.config;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.utils.PaasException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 统一配置ZK管理类，实现初始化自动创建
 *
 */
public class ConfigurationCenter {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationCenter.class);

	private final String UNIX_FILE_SEPARATOR = "/";
	private CountDownLatch latch = new CountDownLatch(1);

	private ZooKeeper zk = null;

	private String centerAddr = null;
	private boolean createZKNode = false;

	private int timeOut = 2000;

	private String runMode = PROD_MODE;// P:product mode; D:dev mode

	public static final String DEV_MODE = "D";
	public static final String PROD_MODE = "P";

	private List<String> configurationFiles = new ArrayList<>();
	private Properties props = new Properties();

	private String PROP_KEY_ZK_AUTH = "com.ai.auth.login.config";

	private HashMap<String, ArrayList<ConfigurationWatcher>> subsMap = new HashMap<>();

	public ConfigurationCenter(String centerAddr, int timeOut, String runMode, List<String> configurationFiles) {
		this.centerAddr = centerAddr;
		this.timeOut = timeOut;
		this.runMode = runMode;
		if (configurationFiles != null) {
			this.configurationFiles.addAll(configurationFiles);
		}
	}

	public ConfigurationCenter(String centerAddr, int timeOut, String runMode) {
		this.centerAddr = centerAddr;
		this.timeOut = timeOut;
		this.runMode = runMode;
	}

	public String getConfAndWatch(String confPath, ConfigurationWatcher warcher) throws PaasException {
		ArrayList<ConfigurationWatcher> watcherList = subsMap.get(confPath);
		if (watcherList == null) {
			watcherList = new ArrayList<>();
			subsMap.put(confPath, watcherList);
		}
		watcherList.add(warcher);
		return this.getConf(confPath);
	}

	public String getConf(String confPath) throws PaasException {
		String conf;
		try {
			if (DEV_MODE.equals(runMode)) {
				return props.getProperty(confPath);
			} else {
				if (null == zk || States.CONNECTED != zk.getState()) {
					// 此时断掉了，需要重连
					zk = connectZookeeper(timeOut, runMode);
				}
				conf = new String(zk.getData(confPath, true, null), "UTF-8");
			}
		} catch (Exception e) {
			log.error("", e);
			throw new PaasException("9999", "failed to get configuration from configuration center", e);
		}
		return conf;
	}

	private synchronized ZooKeeper connectZookeeper(int timeout, String runMode) throws Exception {
		if (null != zk && States.CONNECTED == zk.getState())
			return zk;
		if (DEV_MODE.equals(runMode)) {
			zk = new ZooKeeper(centerAddr, timeout, new Watcher() {
				public void process(WatchedEvent event) {
					// 不做处理，节点变化也可能反映出来
					// 连接建立时, 打开latch, 唤醒wait在该latch上的线程
					if (event.getState() == KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});
			// 增加认证信息
			Object auth = props.getProperty(PROP_KEY_ZK_AUTH);
			if (auth != null) {
				zk.addAuthInfo("digest", auth.toString().getBytes());
			}
			return zk;
		} else {
			zk = new ZooKeeper(centerAddr, timeout, new Watcher() {
				public void process(WatchedEvent event) {
					if (log.isInfoEnabled()) {
						log.info(event.toString());
					}
					// 不做处理，节点变化也可能反映出来
					// 连接建立时, 打开latch, 唤醒wait在该latch上的线程
					if (event.getState() == KeeperState.SyncConnected) {
						latch.countDown();
					}
					if (Event.EventType.NodeDataChanged.equals(event.getType()) && subsMap.size() > 0) {
						String path = event.getPath();
						ArrayList<ConfigurationWatcher> watcherList = subsMap.get(path);
						if (watcherList != null && watcherList.size() > 0) {
							for (ConfigurationWatcher watcher : watcherList) {
								try {
									watcher.process(getConf(path));
								} catch (PaasException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			});
			// 增加认证信息
			Object auth = props.getProperty(PROP_KEY_ZK_AUTH);
			if (auth != null) {
				zk.addAuthInfo("digest", auth.toString().getBytes());
			}
			return zk;
		}
	}

	public void init() {
		try {
			for (String configurationFile : configurationFiles) {
				props.load(this.getClass().getResourceAsStream(configurationFile));
			}
		} catch (IOException e) {
			log.error("Error load proerpties file," + configurationFiles, e);
		}
		// 增加watch,开发模式没有，生产有
		try {
			zk = connectZookeeper(timeOut, runMode);
		} catch (Exception e) {
			log.error("Error connect to Zookeeper," + centerAddr, e);
		}
		subsMap.clear();
		if (isCreateZKNode()) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			writeData();
		}
	}

	private void writeData() {
		// 开始创建节点
		Set keyValue = props.keySet();
		for (Iterator it = keyValue.iterator(); it.hasNext();) {
			String path = (String) it.next();
			String pathValue = props.getProperty(path);
			// 开始创建
			try {
				if (!path.equalsIgnoreCase(PROP_KEY_ZK_AUTH)) {
					setZKPathNode(zk, path, pathValue);
				}
			} catch (Exception e) {
				log.error("Error create to set node data,key=" + path + ",value=" + pathValue, e);
			}
		}
	}

	private void setZKPathNode(ZooKeeper zk, String path, String pathValue) throws Exception {
		if (zk.exists(path, false) == null) {
			createPathNode(zk, path.split(UNIX_FILE_SEPARATOR));
		}
		// 设置值,匹配所有版本
		zk.setData(path, pathValue.getBytes("UTF-8"), -1);
		log.info("Set zk node data: node=" + path + ",value=" + pathValue);
	}

	private void createPathNode(ZooKeeper zk, String[] pathParts) throws Exception {
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < pathParts.length; i++) {
			if (!StringUtil.isBlank(pathParts[i])) {
				path.append(UNIX_FILE_SEPARATOR).append(pathParts[i]);
				String pathString = path.toString();
				try {
					if (zk.exists(pathString, false) == null) {
						// 前面都是空
						zk.create(pathString, null, Ids.OPEN_ACL_UNSAFE,
								CreateMode.PERSISTENT);
					}
				} catch (KeeperException e) {
					if (e.code() != KeeperException.Code.NODEEXISTS)
						throw e;
				}
			}
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public void removeWatcher(String confPath, Class warcherClazz) throws PaasException {
		ArrayList<ConfigurationWatcher> watcherList = subsMap.get(confPath);
		try {
			if (watcherList == null) {
				zk.getData(confPath, false, null);
			} else {
				int size = watcherList.size();
				// ConfigurationWatcher watcher = null;
				for (int i = size - 1; i >= 0; i--) {
					if (watcherList.get(i).getClass().equals(warcherClazz)) {
						watcherList.remove(i);
					}
				}
				if (watcherList.size() == 0) {
					zk.getData(confPath, false, null);
				}
			}
		} catch (Exception e) {
			log.error("", e);
			throw new PaasException("9999", "failed to get configuration from configuration center", e);
		}

	}

	public void addPathAuth(String auth, List<ACL> acls) throws Exception {
		zk.addAuthInfo("digest", auth.getBytes());
		Set keyValue = props.keySet();
		List<ACL> existACL;
		for (Iterator it = keyValue.iterator(); it.hasNext();) {
			String path = (String) it.next();
			if (!path.equalsIgnoreCase(PROP_KEY_ZK_AUTH)) {
				existACL = null;
				try {
					Stat stat = zk.exists(path, false);
					existACL = zk.getACL(path, stat);
				} catch (Exception ignored) {
				}
				if (null != existACL) {
					// 还需要去掉所有anyone的
					for (ACL acl : existACL) {
						if (!acl.getId().getId().equalsIgnoreCase("anyone")) {
							acls.add(acl);
						}
					}
				}
				zk.setACL(path, acls, -1);
			}
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public List<String> getConfigurationFiles() {
		return configurationFiles;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setConfigurationFile(List<String> configurationFile) {
		this.configurationFiles.addAll(configurationFiles);
	}

	public void destory() {
		if (null != zk) {
			try {
				log.info("Start to closing zk client," + zk);
				zk.close();
				log.info("ZK client closed," + zk);
			} catch (InterruptedException e) {
				log.error("Can not close zk client", e);
			}
		}
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isCreateZKNode() {
		return createZKNode;
	}

	public void setCreateZKNode(boolean createZKNode) {
		this.createZKNode = createZKNode;
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws PaasException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"paasContext.xml"});
		ConfigurationCenter confCenter = (ConfigurationCenter) ctx.getBean("confCenter");
		log.error(confCenter.getConf("/com/ai/paas/session/conf"));
	}
}
