package io.pivotal.bookshop.config;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication.Locator;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import io.pivotal.bookshop.domain.BookMaster;

@ClientCacheApplication(logLevel="warn", locators= {
		@Locator(host="localhost", port=10334)		
})
@EnablePdx()
public class GemFireConfiguration {

	@Bean("BookMaster")
	public ClientRegionFactoryBean<Integer, BookMaster> bookMasterRegion (GemFireCache gemfireCache) {
		ClientRegionFactoryBean<Integer, BookMaster> books = new ClientRegionFactoryBean<>();
		books.setCache(gemfireCache);
		books.setClose(true);
		books.setShortcut(ClientRegionShortcut.PROXY);
		return books;
	}
	
}
