package com.amrut.prabhu.distributedtracing;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class RemoteCallCommand extends HystrixCommand<String> {

    private final String remoteUrl;

    public RemoteCallCommand(String remoteUrl){
        super(HystrixCommandGroupKey.Factory.asKey("RemoteCallGroup"));
        this.remoteUrl = remoteUrl;
    }

    @Override
    protected String run() throws Exception {
        return makeRemoteCall();
    }

    @Override
    protected String getFallback() {
        return "Fallback response";
    }

    private String makeRemoteCall() {
        return "Remote response";
    }
}
