package org.example.concurrency;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.ENUM.TaskPriority;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@RequiredArgsConstructor
@ToString(of = {"name", "priority", "sequence"})
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask> {

    private static final AtomicLong SEQUENCE = new AtomicLong();

    @NonNull private final TaskPriority priority;
    @NonNull private final String name;
    @NonNull private final Runnable action;
    private final long sequence = SEQUENCE.getAndIncrement();

    @Override
    public void run() {
        action.run();
    }

    @Override
    public int compareTo(PrioritizedTask other) {
        int byPriority = Integer.compare(this.priority.getOrder(), other.priority.getOrder());
        if (byPriority != 0) {
            return byPriority;
        }
        return Long.compare(this.sequence, other.sequence);
    }
}