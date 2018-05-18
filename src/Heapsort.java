import java.util.List;
import java.util.Comparator;

// a class that can mutate a list for use in heapsort
public class Heapsort<T> {
  // methods
  
  // EFFECT: given a list and two indices, swaps the values at those indices
  void swap(List<T> list, int a, int b) {
    T tempVal = list.get(a);
    list.set(a, list.get(b));
    list.set(b, tempVal);
  }
  
  // EFFECT: given a list and a comparator, sorts the list according to the comparator
  /** Sorts the entire list by the given comparator.
   * @param list
   * @param comp
   */
  void heapsort(List<T> list, Comparator<T> comp) {
    int length = list.size();
    this.heapify(list, comp);
    int lastMember = length - 1;
    while (lastMember >= 0) {
      this.swap(list, 0, lastMember);
      lastMember--;
      this.downheap(list, comp, 0, lastMember);
    }
  }
  
  // TODO: is this necessary? I'll keep it in for now for future optimization.
  // EFFECT: given a list and a comparator, sorts the list according to the comparator
  // only sorts the first "lastItemToSort" items
  /** Sorts the list by comparator from the first item (inclusive) to the "lastItemToSort" item (non-inclusive).
   * @param list
   * @param comp
   * @param lastItemToSort
   */
  void heapsort(List<T> list, Comparator<T> comp, int lastItemToSort) {
    this.heapify(list, comp, lastItemToSort);
    int lastMember = lastItemToSort - 1;
    while (lastMember >= 0) {
      this.swap(list, 0, lastMember);
      lastMember--;
      this.downheap(list, comp, 0, lastMember);
    }
  }
  
  // EFFECT: given a list and a comparator, ensures that the list is a heap 
  // with larger (according to the comparator) values on top (parents)
  void heapify(List<T> list, Comparator<T> comp) {
    int length = list.size();
    for (int i = length / 2; i >= 0; i--) {
      this.downheap(list, comp, i, length - 1);
    }
  }

  // TODO: is this necessary?
  // EFFECT: given a list and a comparator, ensures that the list is a heap 
  // with larger (according to the comparator) values on top (parents)
  // Only modifies the list up until lastItemToSort (non-inclusive). For use with the limited sort
  void heapify(List<T> list, Comparator<T> comp, int lastItemToSort) {
    for (int i = lastItemToSort / 2; i >= 0; i--) {
      this.downheap(list, comp, i, lastItemToSort - 1);
    }
  }
  
  // EFFECT: ensures that the given value moves down the heap 
  // until it is below a parent larger than it.
  void downheap(List<T> list, Comparator<T> comp, int index, int lastMember) {
    int maxVal = index;
    int child1 = (index * 2) + 1;
    int child2 = (index * 2) + 2;
    
    if (child1 <= lastMember) {
      if (comp.compare(list.get(maxVal), list.get(child1)) < 0) {
        maxVal = child1;
      }
    }
    if (child2 <= lastMember) {
      if (comp.compare(list.get(maxVal), list.get(child2)) < 0) {
        maxVal = child2;
      }
    }
    
    if (maxVal != index) {
      this.swap(list, maxVal, index);
      this.downheap(list, comp, maxVal, lastMember);
    }
  }
}
