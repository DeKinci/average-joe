package com.dekinci.bot;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.stat.DoubleMomentStatistics;
import io.jenetics.util.Factory;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class Main {
    public static void main(String[] args) {
        int length = new NeuralNetwork(2, 3, 1).genesSize() * 4 * 8;
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(length, 0.5));
        Engine<BitGene, Double> engine = Engine.builder(Main::eval, gtf).build();

        final EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        Genotype<BitGene> result = engine
                .stream()
                .limit(1000)
                .peek(statistics)
                .collect(EvolutionResult.toBestGenotype());

        System.out.println(statistics);

        check(result.getChromosome().as(BitChromosome.class).toByteArray());
    }

    private static void check(byte[] genes) {
        NeuralNetwork nn = createNN(genes);
        System.out.println(nn);

        int amount = 20;

        double[] results = new double[amount];
        double smallest = Double.MIN_VALUE;
        for (int i = 0; i < results.length; i++) {
            results[i] = computeSin(nn, (double) (i) / amount) * 20;
            if (results[i] < smallest)
                smallest = results[i];
        }

        for (int i = 0; i < results.length; i++)
            results[i] -= smallest;

        for (double res : results) {
            for (int i = 0; i < (int) res; i++)
                System.out.print("-");
            System.out.println(res);
        }

        for (int j = 0; j < results.length; j++) {
            double res = Math.sin((double) j / amount * Math.PI) * 20;
            for (int i = 0; i < (int) res; i++)
                System.out.print("-");
            System.out.println(res);
        }

        System.out.println("Delta: " + smallest);
    }

    private static Double eval(Genotype<BitGene> gt) {
        byte[] genes = gt.getChromosome().as(BitChromosome.class).toByteArray();
        return compute(createNN(genes));
    }

    private static NeuralNetwork createNN(byte[] genes) {
        ByteBuffer allocated = ByteBuffer.wrap(genes);

        ShortBuffer shortBuffer = allocated.asShortBuffer();
        shortBuffer.clear();
        short[] arr = new short[shortBuffer.limit()];
        shortBuffer.get(arr);
        int[] ints = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = arr[i];
        }
        return new NeuralNetwork(2, 3,1, ints);
    }

    private static Double compute(NeuralNetwork nn) {
        double error = 0;
        for (int i = 0; i < 100; i++) {
            double arg = (double) i / 100;
            error += Math.pow(computeSin(nn, arg) - Math.sin(arg * Math.PI), 2);
        }

        return -error;
    }

    private static Double computeSin(NeuralNetwork nn, Double arg) {
        double[] inp = {1, arg};
        return nn.calculate(inp)[0];
    }
}
