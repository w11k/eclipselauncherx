package com.weiglewilczek.xwt.launcher.util;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Column Viewer Sorter support.
 * 
 * @author Ilya Shinkarenko
 * @author Daniela Blank
 * 
 * @param <T>
 */
public abstract class AbstractColumnViewerSorter<T> extends ViewerComparator
		implements EntityComparator<T> {
	/**
	 * Ascending sorting
	 */
	public static final int ASC = 1;

	/**
	 * No sorting
	 */
	public static final int NONE = 0;

	/**
	 * Descending sorting
	 */
	public static final int DESC = -1;

	private int direction = 0;

	private final TableViewerColumn column;
	private final TreeColumn treeColumn;

	private final ColumnViewer viewer;

	/**
	 * @param viewer
	 * @param column
	 */
	public AbstractColumnViewerSorter(ColumnViewer viewer,
			TableViewerColumn column) {
		this.column = column;
		this.treeColumn = null;
		this.viewer = viewer;
		this.column.getColumn().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (AbstractColumnViewerSorter.this.viewer.getComparator() != null) {
					if (AbstractColumnViewerSorter.this.viewer.getComparator() == AbstractColumnViewerSorter.this) {
						int tdirection = AbstractColumnViewerSorter.this.direction;

						if (tdirection == ASC) {
							setSorter(AbstractColumnViewerSorter.this, DESC);
						} else if (tdirection == DESC) {
							setSorter(AbstractColumnViewerSorter.this, NONE);
						}
					} else {
						setSorter(AbstractColumnViewerSorter.this, ASC);
					}
				} else {
					setSorter(AbstractColumnViewerSorter.this, ASC);
				}
			}
		});
	}

	/**
	 * @param viewer
	 * @param column
	 */
	public AbstractColumnViewerSorter(ColumnViewer viewer, TreeColumn column) {
		this.treeColumn = column;
		this.column = null;
		this.viewer = viewer;
		this.treeColumn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (AbstractColumnViewerSorter.this.viewer.getComparator() != null) {
					if (AbstractColumnViewerSorter.this.viewer.getComparator() == AbstractColumnViewerSorter.this) {
						int tdirection = AbstractColumnViewerSorter.this.direction;

						if (tdirection == ASC) {
							setSorter(AbstractColumnViewerSorter.this, DESC);
						} else if (tdirection == DESC) {
							setSorter(AbstractColumnViewerSorter.this, NONE);
						}
					} else {
						setSorter(AbstractColumnViewerSorter.this, ASC);
					}
				} else {
					setSorter(AbstractColumnViewerSorter.this, ASC);
				}
			}
		});
	}

	/**
	 * Sets the sorting order and sorts.
	 * 
	 * @param sorter
	 * @param direction
	 */
	public void setSorter(AbstractColumnViewerSorter<T> sorter, int direction) {
		if (column != null) {
			if (direction == NONE) {
				column.getColumn().getParent().setSortColumn(null);
				column.getColumn().getParent().setSortDirection(SWT.NONE);
				viewer.setComparator(null);
			} else {
				column.getColumn().getParent()
						.setSortColumn(column.getColumn());
				sorter.direction = direction;

				if (direction == ASC) {
					column.getColumn().getParent().setSortDirection(SWT.DOWN);
				} else {
					column.getColumn().getParent().setSortDirection(SWT.UP);
				}

				if (viewer.getComparator() == sorter) {
					viewer.refresh();
				} else {
					viewer.setComparator(sorter);
				}
			}
		} else {
			if (direction == NONE) {
				treeColumn.getParent().setSortColumn(null);
				treeColumn.getParent().setSortDirection(SWT.NONE);
				viewer.setComparator(null);
			} else {
				treeColumn.getParent().setSortColumn(treeColumn);
				sorter.direction = direction;

				if (direction == ASC) {
					treeColumn.getParent().setSortDirection(SWT.DOWN);
				} else {
					treeColumn.getParent().setSortDirection(SWT.UP);
				}

				if (viewer.getComparator() == sorter) {
					viewer.refresh();
				} else {
					viewer.setComparator(sorter);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return direction * doCompare((T) e1, (T) e2);
	}

}