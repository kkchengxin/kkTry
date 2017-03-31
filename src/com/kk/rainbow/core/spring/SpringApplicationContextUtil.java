package com.kk.rainbow.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext springApplicationContext;

	public void setApplicationContext(ApplicationContext paramApplicationContext)
			throws BeansException {
		springApplicationContext = paramApplicationContext;
	}

	public static Object getBean(String paramString) throws BeansException {
		if (springApplicationContext == null)
			throw new BeanCreationException("Spring applicationContext is null");
		return springApplicationContext.getBean(paramString);
	}

	public static boolean isContainBean(String paramString) {
		if (springApplicationContext == null)
			throw new BeanCreationException("Spring applicationContext is null");
		return springApplicationContext.containsBean(paramString);
	}
}