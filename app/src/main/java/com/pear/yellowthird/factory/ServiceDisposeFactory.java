package com.pear.yellowthird.factory;

import com.pear.yellowthird.impl.net.ServiceDisposeImpl;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;

/**
 * 服务器的处理
 */

public class ServiceDisposeFactory {

    ServiceDisposeInterface serviceDispose=new ServiceDisposeImpl();

    private static ServiceDisposeFactory gInstance;

    public static ServiceDisposeFactory getInstance()
    {
        if(null==gInstance)
            gInstance=new ServiceDisposeFactory();
        return gInstance;
    }

    public ServiceDisposeInterface getServiceDispose() {
        return serviceDispose;
    }
}
