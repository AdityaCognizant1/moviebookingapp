# 🎬 Premium Cinema Booking System

A full-stack movie booking application with premium UI/UX, multi-theater support, and real-time seat selection.

## ✨ Features

### 🎭 Multi-Theater System
- **4 Theater Types**: Standard, Premium, IMAX, VIP
- **Dynamic Seating**: 96-180 seats per theater
- **Real-time Availability**: Live seat booking status

### 🎨 Premium UI/UX
- **High-Quality Posters**: TMDB movie posters
- **Cinematic Design**: Professional animations and effects
- **Responsive**: Mobile-optimized for all devices
- **Darkened Unavailable Seats**: Clear visual feedback

### 🔐 Security & Authentication
- **JWT Authentication**: Secure user sessions
- **Role-Based Access**: User and Admin separation
- **Password Encryption**: BCrypt security
- **Admin Panel**: Complete content management

### 🗄️ Database & Persistence
- **PostgreSQL**: Production-ready database
- **Auto Sample Data**: Loads on first deployment
- **Booking History**: Persistent user data
- **Multi-Theater Data**: Complex relational structure

## 🚀 Tech Stack

### Backend
- **Spring Boot 3.2.5**: Java framework
- **PostgreSQL**: Cloud database
- **JWT**: Authentication
- **Maven**: Build tool

### Frontend
- **React 18**: Modern UI framework
- **React Router**: Navigation
- **CSS3**: Custom styling with animations
- **Netlify**: Frontend hosting

### Deployment
- **Railway**: Backend + Database hosting
- **Netlify**: Frontend hosting
- **GitHub**: Source control

## 🎯 Live Demo

- **Frontend**: https://premium-cinema-booking.windsurf.build
- **Backend**: Will be deployed to Railway
- **Features**: Full booking system with sample data

## 📱 Screenshots

### User Experience
- Movie browsing with high-quality posters
- Multi-theater showtime selection
- Interactive seat selection with cinema layout
- Booking confirmation and history

### Admin Panel
- Movie management
- Theater configuration
- Booking analytics
- User management

## 🛠️ Local Development

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## 🚂 Deployment

### Railway (Backend + Database)
1. Connect GitHub repository
2. Set root directory to `backend`
3. Add PostgreSQL service
4. Configure environment variables

### Netlify (Frontend)
1. Connect GitHub repository
2. Set build directory to `frontend`
3. Configure environment variables

## 🎬 Sample Data

Automatically loads on first deployment:
- 6 premium movies with TMDB posters
- 4 theaters with different capacities
- 18 showtimes across multiple theaters
- Sample users for testing

## 🔧 Environment Variables

### Backend (Railway)
```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-secret-key
CORS_ORIGINS=your-frontend-url
APP_LOAD_SAMPLE_DATA=auto
```

### Frontend (Netlify)
```
REACT_APP_API_URL=your-backend-url
REACT_APP_USE_MOCK=false
```

## 📄 License

This project is for demonstration purposes.

---

**Built with ❤️ for premium cinema experiences**
