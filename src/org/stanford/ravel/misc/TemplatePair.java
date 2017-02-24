package org.stanford.ravel.misc;

/**
 * Created by lauril on 2/13/17.
 */
public class TemplatePair {

        private String keyword;
        private String value;

        public TemplatePair(String keyword, String value){
            this.keyword = keyword;
            this.value = value;
        }
        public String getKeyword(){ return keyword; }
        public String getValue(){ return value; }


    @Override
    public String toString() {
        return "(" + keyword +", " + value +")";
    }

    @Override
        public int hashCode() {
            return keyword.hashCode() ^ value.hashCode();
            }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TemplatePair)) return false;
            TemplatePair p = (TemplatePair) o;
            return this.keyword.equals(p.getKeyword()) &&
                    this.value.equals(p.getValue());
        }
}
