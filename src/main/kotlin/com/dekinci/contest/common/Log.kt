package com.dekinci.contest.common

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Log {
    val logger: Logger = LogManager.getLogger()

    fun trace(any: Any) {
        logger.trace(any.toString())
    }

    fun debug(any: Any) {
        logger.debug(any.toString())
    }

    fun info(any: Any) {
        logger.info(any.toString())
    }

    fun warn(any: Any) {
        logger.warn(any.toString())
    }

    fun err(any: Any) {
        logger.error(any.toString())
    }

    fun fatal(any: Any) {
        logger.fatal(any.toString())
    }
}