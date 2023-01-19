package org.grails.forge.api


import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.grails.forge.application.ApplicationType
import spock.lang.Specification

@MicronautTest
class FeatureControllerSpec extends Specification {

    @Inject
    ApplicationTypeClient client

    void "test list features"() {
        when:
        List<FeatureDTO> features = client.features(ApplicationType.DEFAULT_OPTION, RequestInfo.LOCAL).features

        then:
        !features.isEmpty()
    }

    void "test community features"() {
        when:
        List<FeatureDTO> communityFeatures = client.features(ApplicationType.DEFAULT_OPTION, RequestInfo.LOCAL)
                .features.findAll { it.community }

        then:
        communityFeatures.isEmpty()
    }

    void "test list features - spanish"() {
        when:
        List<FeatureDTO> features = client.spanishFeatures(ApplicationType.DEFAULT_OPTION).features
        def mongoGorm = features.find { it.name == 'gorm-mongodb' }

        then:
        mongoGorm.description == 'Configura Gorm para MongoDB para aplicaciones groovy'
        !mongoGorm.isPreview()
        !mongoGorm.isCommunity()
    }

    void "test list default features - spanish"() {
        when:
        List<FeatureDTO> features = client.spanishDefaultFeatures(ApplicationType.DEFAULT_OPTION).features
        def assetPipeline = features.find { it.name == 'asset-pipeline-grails' }

        then:
        assetPipeline.description == 'El activo-Pipeline es un complemento utilizado para administrar y procesar activos estáticos en aplicaciones JVM principalmente a través de Gradle (sin embargo, no es obligatorio). Leer más en https'
        !assetPipeline.isPreview()
        !assetPipeline.isCommunity()
    }

    void "test list default features for application type"() {
        when:
        def features = client.defaultFeatures(ApplicationType.PLUGIN, RequestInfo.LOCAL).features

        then:
        !features.any { it.name == 'geb' }
        features.any { it.name == 'gorm-hibernate5' }

        when:
        features = client.defaultFeatures(ApplicationType.DEFAULT_OPTION, RequestInfo.LOCAL).features

        then:
        features.any { it.name == 'geb' }
    }

    void "test list features for application type"() {
        when:
        def features = client.features(ApplicationType.PLUGIN, RequestInfo.LOCAL).features

        then:
        !features.any { it.name == 'geb' }

        when:
        features = client.features(ApplicationType.DEFAULT_OPTION, RequestInfo.LOCAL).features

        then:
        features.any { it.name == 'gorm-mongodb' }
    }

    void "test list features for application type should NOT return default included features"() {
        when:
        def features = client.features(ApplicationType.WEB, RequestInfo.LOCAL).features

        then:
        !features.any { it.name == 'asset-pipeline-grails' }
    }
}