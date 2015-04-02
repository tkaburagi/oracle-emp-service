package io.pivotal.kaburt.oraemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.cloud.service.common.OracleServiceInfo;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

public class ApplicationInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final Map<Class<? extends ServiceInfo>, String> serviceTypeToProfileName = 
			new HashMap<Class<? extends ServiceInfo>, String>();

	static {
		serviceTypeToProfileName.put(OracleServiceInfo.class, "oracle");
		serviceTypeToProfileName.put(MysqlServiceInfo.class, "mysql");
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		System.out.println("Test");

		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		ConfigurableEnvironment appEnvironment = applicationContext.getEnvironment();

		List<String> profiles = new ArrayList<String>();
		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();

		for (ServiceInfo serviceInfo : serviceInfos) {
			System.out.println("配列に入れるサービスインフォ：" + serviceInfo);
			profiles.add(serviceTypeToProfileName.get(serviceInfo.getClass()));
		}
		
		String[] persistenceProfiles = createProfileNames(profiles.get(0), "cloud");
		
		for (String persistenceProfile : persistenceProfiles) {
			System.out.println("アクティベートするプロファイル：" + persistenceProfile);
			appEnvironment.addActiveProfile(persistenceProfile);
		}
	}
	
    private String[] createProfileNames(String baseName, String suffix) {
        String[] profileNames = {baseName, baseName + "-" + suffix};
        System.out.println("Setting profile names: " + StringUtils.arrayToCommaDelimitedString(profileNames));
        return profileNames;
    }

}
