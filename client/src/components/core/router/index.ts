import { createRouter, createWebHistory } from "vue-router";

const routes = [
    { path: '/:conversation', component: () => import('../../Chat.vue') },
    { path: '/login', component: () => import('../../login/Login.vue') },
];

const router = createRouter({
    history: createWebHistory(),
    routes: routes
});

export default router;