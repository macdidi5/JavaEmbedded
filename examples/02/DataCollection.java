import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class DataCollection<E> {

    @ElementList(inline=true, required=false)
    private List<E> list = new ArrayList<E>();

    public void add(E item) {
        list.add(item);
    }
    
    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

}
