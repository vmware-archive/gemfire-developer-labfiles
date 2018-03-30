package io.pivotal.bookshop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication.Locator;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions;

@ClientCacheApplication(name="GemFire Client", logLevel="warn", locators= {
		@Locator(host="localhost", port=10334)
})
@EnablePdx
@ComponentScan(basePackages="io.pivotal.bookshop.services")
@EnableGemfireRepositories(basePackages ="io.pivotal.bookshop.repositories")
@EnableEntityDefinedRegions(basePackages="io.pivotal.bookshop.domain")
@EnableGemfireCacheTransactions
public class GemFireRepositoryConfig {


}
