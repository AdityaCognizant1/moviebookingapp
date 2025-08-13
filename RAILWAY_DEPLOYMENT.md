# 🚂 Railway Deployment Guide - Complete Backend + Database

## 🎯 Why Railway?
- **All-in-One**: Backend hosting + PostgreSQL database
- **Zero Config**: Automatic environment variables
- **Free Tier**: $5/month credit (enough for development)
- **Easy Setup**: Deploy from GitHub in 5 minutes

## 📋 Step-by-Step Deployment

### Step 1: Create Railway Account
1. Go to [Railway.app](https://railway.app)
2. Sign up with your GitHub account
3. Verify your account

### Step 2: Create New Project
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Connect your GitHub account
4. Select your `moviebookingapp` repository

### Step 3: Add PostgreSQL Database
1. In your Railway project dashboard
2. Click "New Service" → "Database" → "PostgreSQL"
3. Railway automatically creates a PostgreSQL instance
4. Database connection details are auto-configured as environment variables

### Step 4: Configure Backend Service
1. Click "New Service" → "GitHub Repo"
2. Select your repository
3. Set **Root Directory**: `backend`
4. Railway auto-detects Spring Boot and configures build

### Step 5: Set Environment Variables
Railway auto-sets these for PostgreSQL:
- `DATABASE_URL` - Full connection string
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password

Add these manually:
```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-super-secure-256-bit-jwt-secret-key-for-production-use-only-change-this
CORS_ORIGINS=https://premium-cinema-booking.windsurf.build
APP_LOAD_SAMPLE_DATA=auto
```

### Step 6: Deploy
1. Railway automatically builds and deploys
2. You'll get a URL like: `https://your-app-name.railway.app`
3. Sample data loads automatically on first run

### Step 7: Update Frontend
Update your frontend to use the Railway backend:

1. In Netlify dashboard, go to Site Settings → Environment Variables
2. Add: `REACT_APP_API_URL=https://your-app-name.railway.app/api`
3. Add: `REACT_APP_USE_MOCK=false`
4. Redeploy frontend

## 🎉 Result
- ✅ Backend: https://your-app-name.railway.app
- ✅ Database: PostgreSQL with sample data
- ✅ Frontend: https://premium-cinema-booking.windsurf.build
- ✅ Full-stack application with persistent database

## 💰 Cost
- **Railway**: $5/month credit (free tier)
- **Netlify**: Free for frontend
- **Total**: Effectively free for development/demo

## 🔧 Troubleshooting
- Check Railway logs if deployment fails
- Ensure environment variables are set correctly
- Database takes ~2 minutes to initialize
