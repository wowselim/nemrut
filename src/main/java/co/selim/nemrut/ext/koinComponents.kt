package co.selim.nemrut.ext

import org.koin.core.component.KoinComponent

inline fun <reified T : Any> KoinComponent.injectAll(): Lazy<List<T>> =
  lazy { getKoin().getAll<T>() }
