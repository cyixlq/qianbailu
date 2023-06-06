package top.cyixlq.core.net.bean;

import androidx.annotation.Nullable;

public class DnsConfig {

    private String url;

    @Nullable
    private String[] hosts;

    public DnsConfig(String url, @Nullable String[] hosts) {
        this.url = url;
        this.hosts = hosts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(@Nullable String[] hosts) {
        this.hosts = hosts;
    }
}
