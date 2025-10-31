[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/MVuWO52T)

# Group Name: bengbeng33 
## Members: 
662115005 Julaluk
662115040 Ratchanon Danintha
662115047 Watcharapong 

# Frontend deployed URL :

# The Social Anti-Fake News System Phase II
The Social Anti-Fake News system is a system that requires the wisdom of the cloud to help check fake 
news. The users who heard the news can enter the news into the system, so the other users can see all 
the news. The other users can look at the news and vote whether this news is fake or not, and put their 
comments on why they think this is fake or not. The news is considered fake or not fake by the number 
of votes. So on the list of news page, users can filter all news, fake news, or non-fake news.

# Features
-User Authentication
-Add News & View
-News Voting
-Comment with Image 
-Admin Dashboard
-Verify Systems

# Tech Stack
Frontend -> Vue 3 + Vite + Tailwind CSS 
Backend -> Spring Boot (Java 17) 
Database -> MySQL  
File Storage -> Supabase 
Authentication -> Spring Security + JWT 
Deployment -> Vercel (frontend), Render (backend not finish)

# File Structure
Backend/
├── src/main/java/se331/daybreaknews/
│ ├── config/
│ ├── controller/
│ ├── dao/
│ ├── dto/
│ ├── entity/
│ ├── repository/
│ ├── security/
│ └── service/
└── resources/
├── docker-compose.yml
├── mvnw.cmd
└── pom.xml

Frontend/
├── src/
│ ├── components/
│ ├── data/
│ ├── router/
│ ├── services/
│ ├── stores/
│ ├── views/
│ ├── App.vue
│ ├── main.ts
│ ├── style.css
│ └── types.ts
├── public/
├── eslint.config.ts
├── index.html
├── package-lock.json
├── package.json
├── postcss.config.js
├── tailwind.config.js
├── tsconfig.app.json
├── tsconfig.json
├── tsconfig.node.json
├── vercel.json
└── vite.config.ts


# Backend Setup
cd Backend
docker compose up

cd Backend
./mvnw clean compile
./mvnw spring-boot:run
--> backend will start at: http://localhost:8080

# Frontend Setup
cd frontend
npm install
npm run dev
--> Frontend available at: http://localhost:5173


# Environment Variables
## For Backend -> Create .env file in /Backend:
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://your-mysql-host:3306/yourdbname
DB_USER=yourusername
DB_PASSWORD=yourpassword

SUPABASE_URL=https://gdvmhaboyscqbpiwggrn.supabase.co
SUPABASE_ANON_KEY=your-anon-key
SUPABASE_SERVICE_KEY=your-service-key
SUPABASE_BUCKET_NAME=news-images

JWT_SECRET=your-jwt-secret


## For Frontend -> Create .env file in /Frontend:
VITE_BACKEND_URL=http://localhost:8080
VITE_SUPABASE_URL=https://gdvmhaboyscqbpiwggrn.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
