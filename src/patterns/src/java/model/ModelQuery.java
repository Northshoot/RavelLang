package patterns.src.java.model;

/**
 * Created by lauril on 1/23/17.
 */
public interface ModelQuery {

    /**
     * Queries local model storage
     *
     * @return copy of the first record
     */
    Record getFirst();

    /**
     * Queries local model storage
     *
     * @return copy of the first record
     */
    Record getLast();

    /**
     * Queries local model storage
     *  if x >= 0 && x <= current_pos
     * @return copy of the x's record
     * else
     * @return getFirst()
     */
    Record get(int x);

    /**
     * Queries local model storage
     *
     * @return copies of all records
     */
    Record[] all();

}
