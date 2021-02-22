public class Movie {
    private String title;

    public Movie() {};

    public Movie(String t) {
        title = t;
    }

    public String getTitle() {
        return title;
    }

    //Создание узла Movie в базе
    public void initInDB() {
        try ( Connector connector = new Connector( "bolt://localhost:7687", "neo4j", "root" ) )
        {
            connector.CreateNode( this);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //Установка режиссера
    public void DirectedBy(Person person) {
        try ( Connector connector = new Connector( "bolt://localhost:7687", "neo4j", "root" ) )
        {
            connector.CreateRelation( this, person, "directedBy" );
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //Установка жанра
    public void isGenre(Genre genre) {
        try ( Connector connector = new Connector( "bolt://localhost:7687", "neo4j", "root" ) )
        {
            connector.CreateRelation( this, genre, "isGenre" );
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}