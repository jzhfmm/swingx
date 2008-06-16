package org.jdesktop.swingx.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdesktop.swingx.JXTree;

/**
     * A searchable targetting the visible rows of a JXTree.
     * 
     * PENDING: value to string conversion should behave as nextMatch (?) which
     * uses the convertValueToString().
     * 
     */
    public class TreeSearchable extends AbstractSearchable {

        private JXTree tree;


        /**
         * @param tree
         */
        public TreeSearchable(JXTree tree) {
            this.tree = tree;
        }

        @Override
        protected void findMatchAndUpdateState(Pattern pattern, int startRow,
                boolean backwards) {
            SearchResult searchResult = null;
            if (backwards) {
                for (int index = startRow; index >= 0 && searchResult == null; index--) {
                    searchResult = findMatchAt(pattern, index);
                }
            } else {
                for (int index = startRow; index < getSize()
                        && searchResult == null; index++) {
                    searchResult = findMatchAt(pattern, index);
                }
            }
            updateState(searchResult);

        }

        @Override
        protected SearchResult findExtendedMatch(Pattern pattern, int row) {
            return findMatchAt(pattern, row);
        }

        /**
         * Matches the cell content at row/col against the given Pattern.
         * Returns an appropriate SearchResult if matching or null if no
         * matching
         * 
         * @param pattern
         * @param row
         *            a valid row index in view coordinates
         *            a valid column index in view coordinates
         * @return an appropriate <code>SearchResult</code> if matching or
         * null if no matching
         */
        protected SearchResult findMatchAt(Pattern pattern, int row) {
            String text = tree.getStringAt(row);
            if ((text != null) && (text.length() > 0 )) {
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    return createSearchResult(matcher, row, -1);
                }
            }
            return null;
         // this is pre-767-swingx: consistent string api
//            TreePath path = getPathForRow(row);
//            Object value = null;
//            if (path != null) {
//                value = path.getLastPathComponent();
//            }
//            if (value != null) {
//                Matcher matcher = pattern.matcher(value.toString());
//                if (matcher.find()) {
//                    return createSearchResult(matcher, row, -1);
//                }
//            }
//            return null;
        }

        @Override
        protected int getSize() {
            return tree.getRowCount();
        }

        /**
         * @param result
         * @return {@code true} if the {@code result} contains a match;
         *         {@code false} otherwise
         */
        protected boolean hasMatch(SearchResult result) {
            return result.getFoundRow() >= 0;
        }
        

        @Override
        protected void moveMatchMarker() {
            // the common behaviour (JXList, JXTable) is to not
            // move the selection if not found
            if (!hasMatch(lastSearchResult)) {
                return;
            }
            tree.setSelectionRow(lastSearchResult.foundRow);
            tree.scrollRowToVisible(lastSearchResult.foundRow);

        }

    }