package com.ai.paas.ipaas.ips.dao.mapper.bo;

public class IdpsConfigInfo {
    private Integer id;

    private String gmMode;

    private String gmPath;

    private String imageRoot;

    private String imageNameSplit;

    private String imageType;

    private String imageRootNew;

    private String maxActive;

    private String maxIdle;

    private String maxWait;

    private String testOnBorrow;

    private String testOnReturn;

    private String reserveImage;

    private String extent;

    private String quality;

    private String commandSize;

    private String serverUrl;

    private String uploadUrl;

    private String authUrl;

    private String upPath;

    private String upPathNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGmMode() {
        return gmMode;
    }

    public void setGmMode(String gmMode) {
        this.gmMode = gmMode == null ? null : gmMode.trim();
    }

    public String getGmPath() {
        return gmPath;
    }

    public void setGmPath(String gmPath) {
        this.gmPath = gmPath == null ? null : gmPath.trim();
    }

    public String getImageRoot() {
        return imageRoot;
    }

    public void setImageRoot(String imageRoot) {
        this.imageRoot = imageRoot == null ? null : imageRoot.trim();
    }

    public String getImageNameSplit() {
        return imageNameSplit;
    }

    public void setImageNameSplit(String imageNameSplit) {
        this.imageNameSplit = imageNameSplit == null ? null : imageNameSplit.trim();
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType == null ? null : imageType.trim();
    }

    public String getImageRootNew() {
        return imageRootNew;
    }

    public void setImageRootNew(String imageRootNew) {
        this.imageRootNew = imageRootNew == null ? null : imageRootNew.trim();
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive == null ? null : maxActive.trim();
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle == null ? null : maxIdle.trim();
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait == null ? null : maxWait.trim();
    }

    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow == null ? null : testOnBorrow.trim();
    }

    public String getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(String testOnReturn) {
        this.testOnReturn = testOnReturn == null ? null : testOnReturn.trim();
    }

    public String getReserveImage() {
        return reserveImage;
    }

    public void setReserveImage(String reserveImage) {
        this.reserveImage = reserveImage == null ? null : reserveImage.trim();
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent == null ? null : extent.trim();
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality == null ? null : quality.trim();
    }

    public String getCommandSize() {
        return commandSize;
    }

    public void setCommandSize(String commandSize) {
        this.commandSize = commandSize == null ? null : commandSize.trim();
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl == null ? null : serverUrl.trim();
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl == null ? null : uploadUrl.trim();
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl == null ? null : authUrl.trim();
    }

    public String getUpPath() {
        return upPath;
    }

    public void setUpPath(String upPath) {
        this.upPath = upPath == null ? null : upPath.trim();
    }

    public String getUpPathNew() {
        return upPathNew;
    }

    public void setUpPathNew(String upPathNew) {
        this.upPathNew = upPathNew == null ? null : upPathNew.trim();
    }
}