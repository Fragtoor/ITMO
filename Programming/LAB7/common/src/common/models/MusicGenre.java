package common.models;

import java.io.Serializable;

/**
 * Перечисление музыкальных жанров, доступных для музыкальных групп.
 */

public enum MusicGenre implements Serializable {
    /**
     * Джаз
     */
    JAZZ,
    /**
     * Блюз
     */
    BLUES,
    /**
     * Математический рок
     */
    MATH_ROCK,
    /**
     * Пост-рок
     */
    POST_ROCK,
    /**
     * Панк-рок
     */
    PUNK_ROCK
}