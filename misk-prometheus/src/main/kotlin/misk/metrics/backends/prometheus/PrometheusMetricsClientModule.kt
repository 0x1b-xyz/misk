package misk.metrics.backends.prometheus

import io.prometheus.client.CollectorRegistry
import misk.inject.KAbstractModule
import misk.inject.asSingleton
import misk.metrics.backends.prometheus.v2.PrometheusMetricsClientModule as PrometheusMetricsClientModuleV2
import misk.metrics.HistogramRegistry
import misk.metrics.Metrics
import misk.prometheus.PrometheusHistogramRegistry
import jakarta.inject.Inject
import com.google.inject.Provider

/**
 * Binds a [Metrics] implementation whose metrics don't write to a Prometheus infrastructure. For
 * that you should install [PrometheusMetricsServiceModule].
 */
class PrometheusMetricsClientModule : KAbstractModule() {
  override fun configure() {
    bind<HistogramRegistry>().to<PrometheusHistogramRegistry>()
    bind<Metrics>().to<PrometheusMetrics>()
    bind<CollectorRegistry>().toProvider(CollectorRegistryProvider::class.java).asSingleton()

    install(PrometheusMetricsClientModuleV2())
  }

  /**
   * In order to make it possible to install this module multiple times, we make this binding not
   * dependent on the instance of [PrometheusMetricsClientModule] that created it.
   */
  internal class CollectorRegistryProvider @Inject constructor() : Provider<CollectorRegistry> {
    override fun get(): CollectorRegistry {
      return CollectorRegistry()
    }
  }
}
