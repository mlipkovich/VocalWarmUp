package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.sequence.Instrument;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cn.sherlock.com.sun.media.sound.SF2Soundbank;

/**
 * Created by Mikhail Lipkovich on 2/18/2018.
 */
public class SF2Database {

    private SF2Database() {}

    public static Map<Instrument, Program> parseDescription(String str) {
        Map<Instrument, Program> instrumentToProgram = new HashMap<>();
        Scanner scanner = new Scanner(str);
        while (scanner.hasNext()) {
            String line = scanner.next();
            String[] programInstrument = line.split(":");
            Instrument instrument = Instrument.getByCode(programInstrument[0]);
            String[] programArr = programInstrument[1].split(",");
            Program program;
            if (programArr.length == 1) {
                program = new Program(Integer.valueOf(programArr[0]));
            } else {
                program = new Program(Integer.valueOf(programArr[0]), Integer.valueOf(programArr[1]));
            }
            instrumentToProgram.put(instrument, program);
        }
        scanner.close();
        return instrumentToProgram;
    }

    public static SF2Soundbank readSoundbank(InputStream soundbankStream) {
        try {
            return new SF2Soundbank(soundbankStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read soundbank", e);
        }
    }

    public static class Program {
        private int bank;
        private int program;

        public Program(int bank, int program) {
            this.bank = bank;
            this.program = program;
        }

        public Program(int program) {
            this(0, program);
        }

        public int getBank() {
            return bank;
        }

        public int getProgram() {
            return program;
        }
    }
}
