import { createRouter, createWebHashHistory } from 'vue-router'
import ProductList from '../components/ProductList.vue'
import ShoppingCart from '../views/ShoppingCart.vue'
import OrderSuccess from '../views/OrderSuccess.vue'
import NotFound from '../views/NotFound.vue'

const routes = [
  { path: '/', name: 'home', component: ProductList },
  { path: '/cart', name: 'cart', component: ShoppingCart },
  { path: '/order-success', name: 'orderSuccess', component: OrderSuccess },
  { path: '/:pathMatch(.*)*', name: 'notFound', component: NotFound },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

export default router
