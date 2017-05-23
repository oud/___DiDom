package com.didom.myapp.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.didom.myapp.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.UserInfo.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.PaymentType.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.PaymentType.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.PaymentType.class.getName() + ".proposals", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Skill.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Skill.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.HasSkill.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Job.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Job.class.getName() + ".proposals", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Duration.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Duration.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.ProposalStatusCatalog.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.ProposalStatusCatalog.class.getName() + ".proposals", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.ProposalStatusCatalog.class.getName() + ".messages", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Proposal.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Proposal.class.getName() + ".messages", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Message.class.getName(), jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Message.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Message.class.getName() + ".attachments", jcacheConfiguration);
            cm.createCache(com.didom.myapp.domain.Attachment.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
