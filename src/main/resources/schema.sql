INSERT INTO users VALUES(1, "admin@springoc.com", 1, 1, 1, 1, "$2a$10$QwVpZJ4Up9YbeqboJnNPzeqkJgD6IYrMPEeU0G7ye3uIT6aNTPN76", "admin");
INSERT INTO users VALUES(2, "user@springco.com", 1, 1, 1, 1, "$2a$10$QwVpZJ4Up9YbeqboJnNPzeqkJgD6IYrMPEeU0G7ye3uIT6aNTPN76", "user");
INSERT INTO roles VALUES(1, 'ROLE_ADMIN');
INSERT INTO roles VALUES(2, 'ROLE_USER');
INSERT INTO users_roles VALUES(1, 1);
INSERT INTO users_roles VALUES(1, 2);

-- Videos:
INSERT INTO videos VALUES(1, "Przeciętny nastolatek, Peter Parker (Tobey Maguire) przeistacza się w superbohatera pod wpływem ukąszenia przez radioaktywnego pająka. Kiedy jego ukochany wuj zostaje brutalnie zamordowany przez włamywaczy, Peter przysięga sobie, że użyje swoich niezwykłych sił, aby pomścić jego śmierć. Znany odtąd jako \"Spider-Man\", Peter rozpoczyna walkę ze zbrodnią, co prowadzi do nieuchronnego konfliktu ze złowrogim Zielonym Goblinem.", "Spider-Man", "https://fwcdn.pl/fpo/96/95/9695/7518091.3.jpg", 1, "Peter Parker po ugryzieniu przez radioaktywnego pająka zyskuje supermoce...", 2002);
INSERT INTO videos VALUES(2, "Państwo McCallisterowie postanawiają spędzić święta Bożego Narodzenia w Paryżu. Cała rodzina w pośpiechu przygotowuje się do wyjazdu. W zamieszaniu rodzice zapominają zabrać ze sobą jedno z dzieci - ośmioletniego Kevina (Macaulay Culkin). Chłopiec zostaje sam w pustym domu. Początkowo jest bardzo zadowolony, że nareszcie może robić to, na co zawsze miał ochotę. Niebawem jednak orientuje się, że dwaj włamywacze: Harry (Joe Pesci) i Marv (Daniel Stern), mają zamiar okraść posiadłość. Kevin spróbuje samodzielnie ochronić dom.", "Kevin sam w domu", "https://fwcdn.pl/fpo/67/21/6721/7595394.3.jpg", 1, "Rodzina McCallisterów zamierza spędzić Święta Bożego Narodzenia we Francji. W dzień wyjazdu omal nie spóźniają się....", 1990);

INSERT INTO users_videos VALUES(1, 1);
INSERT INTO users_videos VALUES(1, 2);

-- Categories:
INSERT INTO categories VALUES(1, "Komedia");
INSERT INTO categories VALUES(2, "Akcja");

-- VIDOES -> CATEGORIES ASSINGMENT:
INSERT INTO videos_categories VALUES(1, 2);
INSERT INTO videos_categories VALUES(2, 1);

