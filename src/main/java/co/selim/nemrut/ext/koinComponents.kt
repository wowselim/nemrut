package co.selim.nemrut.ext

import org.koin.core.component.KoinComponent
import org.koin.mp.KoinPlatformTools

inline fun <reified T : Any> KoinComponent.injectAll(
  mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
): Lazy<List<T>> =
  lazy(mode) { getKoin().getAll<T>() }
