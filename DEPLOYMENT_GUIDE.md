# 🚀 Premium Cinema Booking System - Deployment Guide

## 📋 Overview
This guide will help you deploy your premium movie booking application with:
- **Frontend**: Already deployed on Netlify (https://premium-cinema-booking.windsurf.build)
- **Backend**: Spring Boot API with PostgreSQL database
- **Database**: Cloud PostgreSQL (Railway/Heroku/Supabase)

## 🎯 Deployment Options

### Option 1: Railway (Recommended - Easiest)

#### Step 1: Create Railway Account
1. Go to [Railway.app](https://railway.app)
2. Sign up with GitHub
3. Create new project

#### Step 2: Add PostgreSQL Database
1. Click "Add Service" → "Database" → "PostgreSQL"
2. Railway will automatically create a PostgreSQL instance
3. Note the connection details (automatically set as environment variables)

#### Step 3: Deploy Backend
1. Click "Add Service" → "GitHub Repo"
2. Connect your GitHub repository
3. Select the `backend` folder as root directory
4. Railway will automatically detect Spring Boot and deploy

#### Step 4: Environment Variables
Railway automatically sets these for PostgreSQL:
- `DATABASE_URL` - Full connection string
- `DATABASE_USERNAME` - Database username  
- `DATABASE_PASSWORD` - Database password

Add these additional variables:
- `SPRING_PROFILES_ACTIVE=prod`
- `JWT_SECRET=your-super-secure-256-bit-jwt-secret-key-for-production-use-only`
- `CORS_ORIGINS=https://premium-cinema-booking.windsurf.build`
- `APP_LOAD_SAMPLE_DATA=auto` (loads sample data on first deployment)

**Sample Data Loading Options:**
- `auto` - Load sample data only if database is empty (recommended)
- `true` - Always load sample data (overwrites existing)
- `false` - Never load sample data

#### Step 5: Update Frontend API URL
Update your frontend's API base URL to point to Railway:
```javascript
const API_BASE = "https://your-app-name.railway.app/api";
```

---

### Option 2: Heroku + PostgreSQL

#### Step 1: Install Heroku CLI
```bash
npm install -g heroku
heroku login
```

#### Step 2: Create Heroku App
```bash
cd backend
heroku create your-cinema-backend
heroku addons:create heroku-postgresql:essential-0
```

#### Step 3: Set Environment Variables
```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set JWT_SECRET=your-super-secure-256-bit-jwt-secret-key
heroku config:set CORS_ORIGINS=https://premium-cinema-booking.windsurf.build
```

#### Step 4: Deploy
```bash
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

---

### Option 3: Supabase Database + Railway/Render

#### Step 1: Create Supabase Database
1. Go to [Supabase.com](https://supabase.com)
2. Create new project
3. Get connection string from Settings → Database

#### Step 2: Deploy to Railway/Render
Use the connection string as `DATABASE_URL` environment variable.

---

## 🔧 Backend Configuration Files Created

### 1. `railway.json` - Railway deployment config
### 2. `Procfile` - Process definition
### 3. `application-prod.properties` - Production configuration
### 4. Updated `pom.xml` - Added PostgreSQL dependency
### 5. Updated `CorsConfig.java` - Environment-based CORS

## 📊 Database Schema
Your application will automatically create these tables:
- `users` - User accounts and authentication
- `movies` - Movie catalog with posters
- `theaters` - Multi-theater support (4 theaters)
- `showtimes` - Movie schedules per theater
- `seats` - Individual seat tracking
- `bookings` - User reservations

## 🎬 Sample Data
The `DataLoader` will automatically populate:
- **6 Movies** with high-quality TMDB posters
- **4 Theaters** (Standard, Premium, IMAX, VIP)
- **Multiple Showtimes** across different theaters
- **Sample Users** for testing

## 🔐 Security Features
- JWT authentication with secure tokens
- Role-based access control (USER/ADMIN)
- Password encryption with BCrypt
- CORS protection for frontend integration
- SQL injection prevention with JPA

## 🌐 Frontend Integration

### Current Status:
✅ **Frontend Deployed**: https://premium-cinema-booking.windsurf.build
✅ **Mock Data**: Works independently for demonstration
✅ **API Ready**: Will connect to backend when deployed

### After Backend Deployment:
1. Update `src/api.js` with your backend URL
2. Set `USE_MOCK_DATA = false` 
3. Redeploy frontend to Netlify

## 🎯 Production Features

### Backend API Endpoints:
- `GET /api/movies` - Movie catalog
- `GET /api/theaters` - Theater information  
- `GET /api/showtimes` - Available showtimes
- `POST /api/bookings` - Create reservations
- `GET /api/seats/showtime/{id}` - Seat availability

### Frontend Features:
- 🎨 High-quality movie posters (TMDB)
- 🎭 Multi-theater support with capacity info
- 🪑 Interactive seat selection with darkened unavailable seats
- 📱 Responsive design for all devices
- ✨ Premium animations and effects

## 🚀 Quick Start Commands

### Railway Deployment:
```bash
# 1. Push to GitHub
git add .
git commit -m "Ready for deployment"
git push origin main

# 2. Connect to Railway
# - Go to railway.app
# - Create project from GitHub repo
# - Add PostgreSQL service
# - Deploy automatically
```

### Local Testing:
```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend  
cd frontend
npm start
```

## 📞 Support
- **Railway**: [docs.railway.app](https://docs.railway.app)
- **Heroku**: [devcenter.heroku.com](https://devcenter.heroku.com)
- **PostgreSQL**: [postgresql.org/docs](https://postgresql.org/docs)

Your premium cinema booking system is ready for production deployment! 🎬✨
