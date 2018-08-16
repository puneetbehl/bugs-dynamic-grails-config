package datastore

import grails.util.Environment
import org.grails.config.PropertySourcesConfig
import org.h2.Driver
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import spock.lang.Specification

class DemoSpec extends Specification {

    void "test dynamic configurations"() {
        given:
        MutablePropertySources mutablePropertySources = new MutablePropertySources()
        mutablePropertySources.addFirst(new MapPropertySource('TestConfig', [
                'dataSource.dbCreate'                              : '',
                'dataSource.url'                                   : 'jdbc:h2:mem:testDb',
                'dataSource.username'                              : 'sa',
                'dataSource.password'                              : '',
                'dataSource.driverClassName'                       : Driver.name,
                'environments.other.dataSource.url'                : 'jdbc:h2:mem:otherDb',
        ]))
        def config

        when:
        System.setProperty(Environment.KEY, Environment.DEVELOPMENT.name)
        config = new PropertySourcesConfig(mutablePropertySources)

        then:
        config.getProperty('dataSource.url') == 'jdbc:h2:mem:testDb'

        when:
        System.setProperty(Environment.KEY, 'other')
        config = new PropertySourcesConfig(mutablePropertySources)

        then:
        config.getProperty('dataSource.url') == 'jdbc:h2:mem:otherDb'
    }

}
