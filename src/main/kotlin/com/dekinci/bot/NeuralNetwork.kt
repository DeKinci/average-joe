package com.dekinci.bot

import com.dekinci.bot.DoubleToGrayCode.doubleToGC
import com.dekinci.bot.DoubleToGrayCode.gCToDouble
import java.util.*

class NeuralNetwork {
    private var inputWeights: Array<DoubleArray> = emptyArray()
    private var innerWeights: Array<Array<DoubleArray>> = emptyArray()
    private var outputWeights: Array<DoubleArray> = emptyArray()

    private var inputSize: Int = 0
    private var innerSize: Int = 0
    private var outputSize: Int = 0

    constructor(inputSize: Int, innerSize: Int, outputSize: Int) {
        this.inputSize = inputSize
        this.outputSize = outputSize
        this.innerSize = innerSize

        inputWeights = Array(inputSize) { DoubleArray(innerSize) }
        innerWeights = Array(NETWORK_DEPTH) { Array(innerSize) { DoubleArray(innerSize) } }
        outputWeights = Array(innerSize) { DoubleArray(outputSize) }

        val r = Random()
        for (i in 0 until inputSize)
            for (j in 0 until innerSize)
                inputWeights[i][j] = r.nextGaussian() / 2

        for (k in 0 until NETWORK_DEPTH)
            for (i in 0 until innerSize)
                for (j in 0 until innerSize)
                    innerWeights[k][i][j] = r.nextGaussian() / 2

        for (i in 0 until innerSize)
            for (j in 0 until outputSize)
                outputWeights[i][j] = r.nextGaussian() / 2
    }

    constructor(inputSize: Int, innerSize: Int, outputSize: Int, genes: IntArray) {
        this.inputSize = inputSize
        this.innerSize = innerSize
        this.outputSize = outputSize

        inputWeights = Array(inputSize) { DoubleArray(innerSize) }
        innerWeights = Array(NETWORK_DEPTH) { Array(innerSize) { DoubleArray(innerSize) } }
        outputWeights = Array(innerSize) { DoubleArray(outputSize) }

        var index = 0
        for (i in 0 until inputSize)
            for (j in 0 until innerSize)
                inputWeights[i][j] = gCToDouble(genes[index++])

        for (k in 0 until NETWORK_DEPTH)
            for (i in 0 until innerSize)
                for (j in 0 until innerSize)
                    innerWeights[k][i][j] = gCToDouble(genes[index++])

        for (i in 0 until innerSize)
            for (j in 0 until outputSize)
                outputWeights[i][j] = gCToDouble(genes[index++])
    }

    fun genotype(): IntArray {
        val genesAmount = inputSize * innerSize + innerSize * innerSize * NETWORK_DEPTH + innerSize * outputSize
        val genes = IntArray(genesAmount)
        var index = 0
        for (i in 0 until inputSize)
            for (j in 0 until innerSize)
                genes[index++] = doubleToGC(inputWeights[i][j])

        for (k in 0 until NETWORK_DEPTH)
            for (i in 0 until innerSize)
                for (j in 0 until innerSize)
                    genes[index++] = doubleToGC(innerWeights[k][i][j])

        for (i in 0 until innerSize)
            for (j in 0 until outputSize)
                genes[index++] = doubleToGC(outputWeights[i][j])

        return genes
    }

    fun calculate(input: DoubleArray): DoubleArray {
//        for (int i = 0; i < inputSize; i++)
//            valuesInp[i] = activationF(input[i]);



//        println("Input: " + Arrays.toString(input))

        val valuesInnInn = Array(2) { DoubleArray(innerSize) }

        (0 until innerSize).forEach {innerNeuron ->
            val sum = input.mapIndexed { inputNeuron, neuronVal -> neuronVal * inputWeights[inputNeuron][innerNeuron] }.sum()
            valuesInnInn[0][innerNeuron] = activationF(sum)
        }

//        println("Layer: " + Arrays.toString(valuesInnInn[0]))

        var innInnIndex = 0
        while (innInnIndex < NETWORK_DEPTH) {
            val from = innInnIndex % 2
            val to = 1 - from

            (0 until innerSize).forEach { toNeuron ->
                val sum = valuesInnInn[from].mapIndexed { fromNeuron, neuronVal -> neuronVal * innerWeights[innInnIndex][fromNeuron][toNeuron] }.sum()
                valuesInnInn[to][toNeuron] = activationF(sum)
            }

//            println("Layer: " + Arrays.toString(valuesInnInn[to]))
            innInnIndex++
        }

        val from = innInnIndex % 2
        val result = DoubleArray(outputSize)
        (0 until outputSize).forEach { outNeuron ->
            val sum = valuesInnInn[from].mapIndexed { innerNeuron, d -> d * outputWeights[innerNeuron][outNeuron] }.sum()
            result[outNeuron] = activationF(sum)
        }

        return result
    }

    fun genesSize(): Int {
        return inputSize * innerSize + innerSize * innerSize * NETWORK_DEPTH + innerSize * outputSize
    }

    override fun toString(): String {
        val joiner = StringJoiner("\n")
        joiner.add("---INPUT---")
        for (layer in inputWeights)
            joiner.add(Arrays.toString(layer))

        joiner.add("---INNER---")
        for (layers in innerWeights) {
            joiner.add("------")
            for (layer in layers)
                joiner.add(Arrays.toString(layer))
        }

        joiner.add("---OUTPUT---")
        for (layer in outputWeights)
            joiner.add(Arrays.toString(layer))

        return joiner.toString()
    }

    companion object {
        private val NETWORK_DEPTH = 3

        /**
         * sigma
         */
        private fun activationF(x: Double): Double {
//            return 1 / (1 + Math.exp(-x))
            return Math.tanh(x)
        }
    }
}
