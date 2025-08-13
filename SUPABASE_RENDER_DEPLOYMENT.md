# 🗄️ Supabase + Render Deployment Guide

## 🎯 Why This Combo?
- **Supabase**: Excellent PostgreSQL database (free tier)
- **Render**: Reliable backend hosting (free tier)
- **Both have generous free tiers**

## 📋 Step-by-Step Deployment

### Step 1: Create Supabase Database
1. Go to [Supabase.com](https://supabase.com)
2. Sign up and create new project
3. Choose region closest to your users
4. Wait for database to initialize (~2 minutes)

### Step 2: Get Database Connection
1. Go to Settings → Database
2. Copy the connection string:
   ```
   postgresql://postgres:[YOUR-PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
   ```

### Step 3: Deploy Backend to Render
1. Go to [Render.com](https://render.com)
2. Sign up with GitHub
3. Click "New" → "Web Service"
4. Connect your GitHub repository
5. Configure:
   - **Root Directory**: `backend`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -Dserver.port=$PORT -jar target/backend-0.0.1-SNAPSHOT.jar`

### Step 4: Set Environment Variables in Render
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=your-supabase-connection-string
JWT_SECRET=your-super-secure-256-bit-jwt-secret
CORS_ORIGINS=https://premium-cinema-booking.windsurf.build
APP_LOAD_SAMPLE_DATA=auto
```

### Step 5: Deploy and Test
1. Render builds and deploys automatically
2. You'll get a URL like: `https://your-app-name.onrender.com`
3. Sample data loads on first startup

### Step 6: Update Frontend
Same as Railway - update Netlify environment variables.

## 💰 Cost
- **Supabase**: Free tier (500MB database)
- **Render**: Free tier (750 hours/month)
- **Netlify**: Free for frontend
- **Total**: Completely free!
