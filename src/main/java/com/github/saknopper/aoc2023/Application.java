package com.github.saknopper.aoc2023;

import java.util.Set;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.saknopper.aoc2023.days.Day;

public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private static final Set<String> SKIP_PART_ONE_DAYS = Set.of();
    private static final Set<String> SKIP_PART_TWO_DAYS = Set.of();

    public static void main(String[] args) {
        LOG.info("---------------------------------------------");
        LOG.info("Advent of Code 2023");
        LOG.info("---------------------------------------------");
        LOG.info("");

        IntStream.rangeClosed(1, 25).boxed().forEach(day -> {
            final String dayNr = String.format("%02d", day);

            try {
                @SuppressWarnings("unchecked")
                final Class<? extends Day> clazz = (Class<? extends Day>) Class
                        .forName(Application.class.getPackageName() + ".days.Day" + dayNr);
                final Day dayInstance = clazz.getDeclaredConstructor().newInstance();
                if (!SKIP_PART_ONE_DAYS.contains(dayNr))
                    LOG.info("Day {} part 1: {}", dayNr, dayInstance.getAnswerPartOne());
                else
                    LOG.info("Day {} part 1: {}", dayNr, "Skipping, too slow...");

                if (!SKIP_PART_TWO_DAYS.contains(dayNr))
                    LOG.info("Day {} part 2: {}", dayNr, dayInstance.getAnswerPartTwo());
                else
                    LOG.info("Day {} part 2: {}", dayNr, "Skipping, too slow...");
            } catch (ClassNotFoundException e) {
                /* ignore, will be fixed another day :) */
            } catch (Exception e) {
                LOG.error("Error running day", e);
            }
        });
    }
}
