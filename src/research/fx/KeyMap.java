// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CMap;
import goryachev.common.util.D;
import goryachev.fx.FxAction;
import goryachev.fx.FxButton;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;


/**
 * Key/Accelerator Map.
 */
public class KeyMap<T>
{
	private CMap<T,String> keymap = new CMap<>();
	
	
	public KeyMap()
	{
	}
	
	
	// TODO: or mac(), windows(), linux()
	public void map(T key, String typed)
	{
		keymap.put(key, typed);
	}
	
	
	public void set(FxButton b, T key)
	{
		b.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyPress(key, b, ev, null)); // TODO
	}
	
	
	public void set(Node n, T key, FxAction a)
	{
		n.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyPress(key, n, ev, a));
	}
	
	
	protected void handleKeyPress(T key, Node src, KeyEvent ev, FxAction a)
	{
		D.print(ev, ev.getSource() == src);
		
		String ks = keymap.get(key);
		if(ks != null)
		{
			String k = ev.getCharacter();
			if(ks.equals(k))
			{
				a.fire();
			}
		}
	}
}
