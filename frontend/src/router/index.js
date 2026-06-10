import { createRouter, createWebHashHistory } from 'vue-router'
import ProductList from '../components/ProductList.vue'
import ProductDetail from '../components/ProductDetail.vue'
import ShoppingCart from '../views/ShoppingCart.vue'
import OrderSuccess from '../views/OrderSuccess.vue'
import OrderList from '../views/OrderList.vue'
import OrderDetail from '../views/OrderDetail.vue'
import Login from '../views/Login.vue'
import NotFound from '../views/NotFound.vue'

const routes = [
  { path: '/', name: 'home', component: ProductList },
  { path: '/product/:id', name: 'productDetail', component: ProductDetail, props: true },
  { path: '/cart', name: 'cart', component: ShoppingCart },
  { path: '/orders', name: 'orders', component: OrderList },
  { path: '/order/:orderNo', name: 'orderDetail', component: OrderDetail },
  { path: '/order-success', name: 'orderSuccess', component: OrderSuccess },
  { path: '/login', name: 'login', component: Login },
  { path: '/:pathMatch(.*)*', name: 'notFound', component: NotFound },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

export default router
