import {createBrowserRouter} from 'react-router'
import LandingPage from '../pages/LandingPage.jsx'
import GrowWithUs from "../pages/GrowWithUs.jsx";
import AuthPage from "../pages/AuthPage.jsx";

const router = createBrowserRouter([
    {
        path:"/",
        element:<LandingPage />
    },
    {
        path:"/landing",
        element:<LandingPage />
    },
    {
        path:"/grow",
        element:<GrowWithUs />
    },
    {
        path:"/register",
        element:<AuthPage />
    }
])

export default router;