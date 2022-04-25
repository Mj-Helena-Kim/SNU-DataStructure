import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<Genre> genreList;

    public MovieDB() {
    	genreList = new MyLinkedList<Genre>();
    }

    public void insert(MovieDBItem item) {
		boolean found = false;
		Genre toAddGenre = new Genre(item.getGenre());

		for(Genre g : genreList){
			int compare = g.getItem().compareTo(item.getGenre());
			if(compare < 0) continue;
			else if (compare == 0){
				found = true;
				toAddGenre = g;
				break;
			}
			else{ 
				found = true;
				genreList.add(toAddGenre, g);
				break;
			}
		}

		if(!found)
			genreList.add(toAddGenre);
			
		toAddGenre.getList().add(item.getTitle());
    }

    public void delete(MovieDBItem item) {
		for(Genre g: genreList){
			for(String t: g.myMovieList){
				MovieDBItem toBeDeleted = new MovieDBItem(g.getItem(), t);
				if(toBeDeleted.compareTo(item) == 0){
					g.myMovieList.remove(item.getTitle());
					if(g.myMovieList.isEmpty())
						genreList.remove(g);
					return;
				}
			}
		}
	}

    public MyLinkedList<MovieDBItem> search(String term) {
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		for(Genre g: genreList){
			for(String t: g.myMovieList){
				if(t.contains(term)){
					MovieDBItem toReturn = new MovieDBItem(g.getItem(), t);
					results.add(toReturn);
				}
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		for(Genre g : genreList){
			for(String t : g.myMovieList){
				MovieDBItem toAdd = new MovieDBItem(g.getItem(), t);
				results.add(toAdd);
			}
		}
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	MovieList myMovieList;

	public Genre(String name) {
		super(name);
		myMovieList = new MovieList();
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		return this.getItem().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == (obj.hashCode());
	}

	public MovieList getList(){
		return myMovieList;
	}
}

class MovieList implements ListInterface<String> {	

	MyLinkedList<String> MyMovieList; // list of title

	public MovieList() {
		MyMovieList = new MyLinkedList<String>();
	}

	@Override
	public Iterator<String> iterator() {
		return MyMovieList.iterator();
	}

	@Override
	public boolean isEmpty() {
		return MyMovieList.isEmpty();
	}

	@Override
	public int size() {
		return MyMovieList.size();
	}

	@Override
	public String first() {
		return MyMovieList.head.getNext().getItem();
	}
	
	@Override
	public void add(String item) {
		boolean flag = true;
		for(String i : MyMovieList){
			int compare = i.compareTo(item);
			if(compare == 0){
				flag = false;
				break;
			}
			else if(compare > 0){
				MyMovieList.add(item, i);
				flag = false;
				break;
			}
		}

		if(flag)
			MyMovieList.add(item);
	}

	public void remove(String item){
		MyMovieList.remove(item);
	}

	@Override
	public void removeAll() {
		MyMovieList.removeAll();
	}
}