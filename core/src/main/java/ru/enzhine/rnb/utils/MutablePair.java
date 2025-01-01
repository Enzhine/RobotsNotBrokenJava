package ru.enzhine.rnb.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class MutablePair<A, B> {
    private A left;
    private B right;
}
