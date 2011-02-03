package com.puzzletimer.scramblers;

import java.util.Random;

import com.puzzletimer.models.Scramble;
import com.puzzletimer.models.ScramblerInfo;
import com.puzzletimer.solvers.RubiksDominoSolver;

public class RubiksDominoRandomScrambler implements Scrambler {
    private ScramblerInfo scramblerInfo;
    private Random random;

    public RubiksDominoRandomScrambler(ScramblerInfo scramblerInfo) {
        this.scramblerInfo = scramblerInfo;
        this.random = new Random();
    }

    public ScramblerInfo getScramblerInfo() {
        return this.scramblerInfo;
    }

    public Scramble getNextScramble() {
        return new Scramble(
            getScramblerInfo().getScramblerId(),
            RubiksDominoSolver.generate(
                RubiksDominoSolver.getRandomState(this.random)));
    }

    @Override
    public String toString() {
        return getScramblerInfo().getDescription();
    }
}
