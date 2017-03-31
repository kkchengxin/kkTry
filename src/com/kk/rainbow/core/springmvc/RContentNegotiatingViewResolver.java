package com.kk.rainbow.core.springmvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class RContentNegotiatingViewResolver extends
		WebApplicationObjectSupport implements ViewResolver, Ordered,
		InitializingBean {
	private static final Log logger = LogFactory
			.getLog(RContentNegotiatingViewResolver.class);
	private int order = -2147483648;
	private ContentNegotiationManager contentNegotiationManager;
	private ContentNegotiationManagerFactoryBean cnManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
	private boolean useNotAcceptableStatusCode = false;
	private List<View> defaultViews;
	private List<ViewResolver> viewResolvers;
	private static final View NOT_ACCEPTABLE_VIEW = new View() {
		public String getContentType() {
			return null;
		}

		public void render(Map<String, ?> paramAnonymousMap,
				HttpServletRequest paramAnonymousHttpServletRequest,
				HttpServletResponse paramAnonymousHttpServletResponse) {
			paramAnonymousHttpServletResponse.setStatus(406);
		}
	};

	public void setOrder(int paramInt) {
		this.order = paramInt;
	}

	public int getOrder() {
		return this.order;
	}

	public void setContentNegotiationManager(
			ContentNegotiationManager paramContentNegotiationManager) {
		this.contentNegotiationManager = paramContentNegotiationManager;
	}

	/** @deprecated */
	public void setFavorPathExtension(boolean paramBoolean) {
		this.cnManagerFactoryBean.setFavorParameter(paramBoolean);
	}

	/** @deprecated */
	public void setUseJaf(boolean paramBoolean) {
		this.cnManagerFactoryBean.setUseJaf(paramBoolean);
	}

	/** @deprecated */
	public void setFavorParameter(boolean paramBoolean) {
		this.cnManagerFactoryBean.setFavorParameter(paramBoolean);
	}

	/** @deprecated */
	public void setParameterName(String paramString) {
		this.cnManagerFactoryBean.setParameterName(paramString);
	}

	/** @deprecated */
	public void setIgnoreAcceptHeader(boolean paramBoolean) {
		this.cnManagerFactoryBean.setIgnoreAcceptHeader(paramBoolean);
	}

	/** @deprecated */
	public void setMediaTypes(Map<String, String> paramMap) {
		if (paramMap != null)
			this.cnManagerFactoryBean.getMediaTypes().putAll(paramMap);
	}

	/** @deprecated */
	public void setDefaultContentType(MediaType paramMediaType) {
		this.cnManagerFactoryBean.setDefaultContentType(paramMediaType);
	}

	public void setUseNotAcceptableStatusCode(boolean paramBoolean) {
		this.useNotAcceptableStatusCode = paramBoolean;
	}

	public void setDefaultViews(List<View> paramList) {
		this.defaultViews = paramList;
	}

	public void setViewResolvers(List<ViewResolver> paramList) {
		this.viewResolvers = paramList;
	}

	protected void initServletContext(ServletContext paramServletContext) {
		Collection localCollection = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(getApplicationContext(),
						InitializingBean.class).values();
		Object localObject;
		if (this.viewResolvers == null) {
			this.viewResolvers = new ArrayList(localCollection.size());
			Iterator localIterator = localCollection.iterator();
			while (localIterator.hasNext()) {
				localObject = (InitializingBean) localIterator.next();
				if (this != localObject)
					this.viewResolvers.add((ViewResolver) localObject);
			}
		} else {
			for (int i = 0; i < this.viewResolvers.size(); i++)
				if (!localCollection.contains(this.viewResolvers.get(i))) {
					localObject = ((InitializingBean) this.viewResolvers.get(i))
							.getClass().getName() + i;
					getApplicationContext().getAutowireCapableBeanFactory()
							.initializeBean(this.viewResolvers.get(i),
									(String) localObject);
				}
		}
		if (this.viewResolvers.isEmpty())
			logger.warn("Did not find any ViewResolvers to delegate to; please configure them using the 'viewResolvers' property on the RContentNegotiatingViewResolver");
		OrderComparator.sort(this.viewResolvers);
		this.cnManagerFactoryBean.setServletContext(paramServletContext);
	}

	public void afterPropertiesSet() throws Exception {
		if (this.contentNegotiationManager == null) {
			this.cnManagerFactoryBean.afterPropertiesSet();
			this.contentNegotiationManager = this.cnManagerFactoryBean
					.getObject();
		}
	}

	public View resolveViewName(String paramString, Locale paramLocale)
			throws Exception {
		RequestAttributes localRequestAttributes = RequestContextHolder
				.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class,
				localRequestAttributes);
		List localList1 = getMediaTypes(((ServletRequestAttributes) localRequestAttributes)
				.getRequest());
		if (localList1 != null) {
			List localList2 = getCandidateViews(paramString, paramLocale,
					localList1);
			View localView = getBestView(localList2, localList1,
					localRequestAttributes);
			if (localView != null)
				return localView;
		}
		if (this.useNotAcceptableStatusCode) {
			if (logger.isDebugEnabled())
				logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
			return NOT_ACCEPTABLE_VIEW;
		}
		logger.debug("No acceptable view found; returning null");
		return null;
	}

	protected List<MediaType> getMediaTypes(
			HttpServletRequest paramHttpServletRequest) {
		try {
			ServletWebRequest localServletWebRequest = new ServletWebRequest(
					paramHttpServletRequest);
			List localList1 = this.contentNegotiationManager
					.resolveMediaTypes(localServletWebRequest);
			List localList2 = getProducibleMediaTypes(paramHttpServletRequest);
			LinkedHashSet localLinkedHashSet = new LinkedHashSet();
			Object localObject = localList1.iterator();
			while (((Iterator) localObject).hasNext()) {
				MediaType localMediaType1 = (MediaType) ((Iterator) localObject)
						.next();
				Iterator localIterator = localList2.iterator();
				while (localIterator.hasNext()) {
					MediaType localMediaType2 = (MediaType) localIterator
							.next();
					if (localMediaType1.isCompatibleWith(localMediaType2))
						localLinkedHashSet.add(getMostSpecificMediaType(
								localMediaType1, localMediaType2));
				}
			}
			localObject = new ArrayList(localLinkedHashSet);
			MediaType.sortBySpecificityAndQuality((List) localObject);
			if (logger.isDebugEnabled())
				logger.debug("Requested media types are " + localObject
						+ " based on Accept header types "
						+ "and producible media types " + localList2 + ")");
			return (List<MediaType>) localObject;
		} catch (HttpMediaTypeNotAcceptableException localHttpMediaTypeNotAcceptableException) {
		}
		return null;
	}

	private List<MediaType> getProducibleMediaTypes(
			HttpServletRequest paramHttpServletRequest) {
		Set localSet = (Set) paramHttpServletRequest
				.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		if (!CollectionUtils.isEmpty(localSet))
			return new ArrayList(localSet);
		return Collections.singletonList(MediaType.ALL);
	}

	private MediaType getMostSpecificMediaType(MediaType paramMediaType1,
			MediaType paramMediaType2) {
		paramMediaType2 = paramMediaType2.copyQualityValue(paramMediaType1);
		return MediaType.SPECIFICITY_COMPARATOR.compare(paramMediaType1,
				paramMediaType2) < 0 ? paramMediaType1 : paramMediaType2;
	}

	private List<View> getCandidateViews(String paramString,
			Locale paramLocale, List<MediaType> paramList) throws Exception {
		ArrayList localArrayList = new ArrayList();
		Iterator localIterator1 = this.viewResolvers.iterator();
		while (localIterator1.hasNext()) {
			ViewResolver localViewResolver = (ViewResolver) localIterator1
					.next();
			View localView = localViewResolver.resolveViewName(paramString,
					paramLocale);
			if (localView != null)
				localArrayList.add(localView);
			Iterator localIterator2 = paramList.iterator();
			while (localIterator2.hasNext()) {
				MediaType localMediaType = (MediaType) localIterator2.next();
				List localList = this.contentNegotiationManager
						.resolveFileExtensions(localMediaType);
				Iterator localIterator3 = localList.iterator();
				while (localIterator3.hasNext()) {
					String str1 = (String) localIterator3.next();
					String str2 = paramString + "." + str1;
					localView = localViewResolver.resolveViewName(str2,
							paramLocale);
					if (localView != null)
						localArrayList.add(localView);
				}
			}
		}
		if (!CollectionUtils.isEmpty(this.defaultViews))
			localArrayList.addAll(this.defaultViews);
		return localArrayList;
	}

	private View getBestView(List<View> paramList, List<MediaType> paramList1,
			RequestAttributes paramRequestAttributes) {
		Iterator localIterator1 = paramList1.iterator();
		while (localIterator1.hasNext()) {
			MediaType localMediaType = (MediaType) localIterator1.next();
			Iterator localIterator2 = paramList.iterator();
			while (localIterator2.hasNext()) {
				View localView = (View) localIterator2.next();
				Object localObject;
				if (((localView instanceof SmartView))
						&& (!localMediaType
								.includes(MediaType.APPLICATION_JSON))
						&& (!localMediaType
								.includes(MediaType.APPLICATION_JSON))
						&& (!localMediaType.includes(MediaType.APPLICATION_XML))
						&& (!localMediaType.includes(MediaType.TEXT_XML))
						&& (!localMediaType.includes(MediaType.TEXT_XML))) {
					localObject = (SmartView) localView;
					if (((SmartView) localObject).isRedirectView()) {
						if (logger.isDebugEnabled())
							logger.debug("Returning redirect view ["
									+ localView + "]");
						return localView;
					}
				}
				if (StringUtils.hasText(localView.getContentType())) {
					localObject = MediaType.parseMediaType(localView
							.getContentType());
					if (localMediaType
							.isCompatibleWith((MediaType) localObject)) {
						if (logger.isDebugEnabled())
							logger.debug("Returning [" + localView
									+ "] based on requested media type '"
									+ localMediaType + "'");
						paramRequestAttributes.setAttribute(
								View.SELECTED_CONTENT_TYPE, localMediaType, 0);
						return localView;
					}
				}
			}
		}
		return null;
	}
}