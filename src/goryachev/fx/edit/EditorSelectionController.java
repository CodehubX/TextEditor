// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * FxEditor Selection Controller.
 * FIX I need to redo this.
 */
public class EditorSelectionController
{
	protected final FxEditor editor;
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	protected final ReadOnlyObjectWrapper<EditorSelection> selectionProperty = new ReadOnlyObjectWrapper(EditorSelection.EMPTY);


	public EditorSelectionController(FxEditor ed)
	{
		this.editor = ed;
	}


	public void clear()
	{
		segments.clear();
	}
	
	
	public boolean isInsideSelection(Marker pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	public void addSelectionSegment(Marker anchor, Marker caret)
	{
		segments.add(new SelectionSegment(anchor, caret));
	}
	
	
	public void setSelection(Marker anchor, Marker caret)
	{
		clear();
		addSelectionSegment(anchor, caret);
	}
	
	
	public void setSelection(Marker m)
	{
		setSelection(m, m);
	}
	
	
	public void clearAndExtendLastSegment(Marker pos)
	{
		Marker anchor = lastAnchor();
		if(anchor == null)
		{
			anchor = pos;
		}
		
		setSelection(anchor, pos);
	}
	
	
	public void extendLastSegment(Marker pos)
	{
		if(pos != null)
		{
			int ix = segments.size() - 1;
			if(ix < 0)
			{
				 addSelectionSegment(pos, pos);
			}
			else
			{
				SelectionSegment s = segments.get(ix);
				Marker anchor = s.getAnchor();
				segments.set(ix, new SelectionSegment(anchor, pos));
			}
		}
	}
	
	
	public Marker lastAnchor()
	{
		int ix = segments.size() - 1;
		if(ix >= 0)
		{
			SelectionSegment s = segments.get(ix);
			return s.getAnchor();
		}
		return null;
	}
	
	
	public SelectionSegment getLastSegment()
	{
		int sz = segments.size();
		if(sz > 0)
		{
			return segments.get(sz - 1);
		}
		return null;
	}


	/** combines overlapping segments and sets selection property */
	// FIX
	public void commitSelection()
	{
		int sz = segments.size();
		if(sz >= 2)
		{
			SelectionSegment last = segments.get(sz - 1);
			
			// starting from the last segment
			for(int i=segments.size()-2; i>=0; --i)
			{
				SelectionSegment seg = segments.get(i);
				SelectionSegment combined = last.swallow(seg);
				if(combined != null)
				{
					segments.set(sz - 1, combined);
					segments.remove(i);
				}
			}
		}
		
		SelectionSegment[] sel = segments.toArray(new SelectionSegment[segments.size()]);
		selectionProperty.set(new EditorSelection(sel));
	}
}
