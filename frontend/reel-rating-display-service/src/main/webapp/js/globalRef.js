"using strict;"

const guiPort = 30400;
const authPort = 30500;
const movieDataPort = 30501;
const reviewsPort = 30502;
const ratingsPort = 30503;
const actorPort = 30504;
const searchPort = 30505;
export class GlobalRef{
    constructor(){
        if(window.location.port === `${guiPort}`){
            this.homeLocation = `http://moxie.cs.oswego.edu:${guiPort}/views/home.html`;
            this.indexLocation = `http://moxie.cs.oswego.edu:${guiPort}/index.html`;
        }
        else{
            this.homeLocation = `http://localhost:5500/frontend/reel-rating-display-service/src/main/webapp/views/home.html`;
            this.indexLocation = `http://localhost:5500/frontend/reel-rating-display-service/src/main/webapp/index.html`;
            // this.logInPath = `http://127.0.0.1:${authPort}/reel-rating-auth-service/auth/login`;
            // this.regPath = `http://127.0.0.1:${authPort}/reel-rating-auth-service/auth/register`;
            //this.movieDataBase = `http://127.0.0.1:${movieDataPort}/reel-rating-movie-data-service`;
            // this.movieImgBase = `http://127.0.0.1:${movieDataPort}/reel-rating-movie-data-service/movie/getMovieImage`;
            // this.reviewBase = `http://127.0.0.1:${reviewsPort}/reel-rating-review-data-service`;
            // this.ratingsBase = `http://127.0.0.1:${ratingsPort}/reel-rating-rating-data-service`;
            // this.actorBase = `http://127.0.0.1:${actorPort}/reel-rating-actor-data-service`;
            //this.searchBase = `http://127.0.0.1:${searchPort}/reel-rating-search-service`;
        }

        this.logInPath = `http://moxie.cs.oswego.edu:${authPort}/reel-rating-auth-service/auth/login`;
        this.regPath = `http://moxie.cs.oswego.edu:${authPort}/reel-rating-auth-service/auth/register`;
        this.movieDataBase = `http://moxie.cs.oswego.edu:${movieDataPort}/reel-rating-movie-data-service`;
        this.movieImgBase = `http://moxie.cs.oswego.edu:${movieDataPort}/reel-rating-movie-data-service/movie/getMovieImage`;
        this.reviewBase = `http://moxie.cs.oswego.edu:${reviewsPort}/reel-rating-review-data-service`;
        this.ratingsBase = `http://moxie.cs.oswego.edu:${ratingsPort}/reel-rating-rating-data-service`;
        this.actorBase = `http://moxie.cs.oswego.edu:${actorPort}/reel-rating-actor-data-service`;
        this.searchBase = `http://moxie.cs.oswego.edu:${searchPort}/reel-rating-search-service`;
        
        
        this.regExSpecChar = /\W/; //Is not word char nor digit
        this.regExNum = /\d/;
        this.regExEmail = /^\w+?@\w+\.\w+/;
    }
}