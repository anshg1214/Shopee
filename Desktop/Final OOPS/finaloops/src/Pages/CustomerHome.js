import React from 'react'
import Navbar from './Navbar'
import Carousel from './Carousel'
import Pagination from './Pagination'
import ToTopBtn from './ToTopBtn'
import Footer from './Footer'
import Product from './Product'
import CartList from './CartList'
import { useState } from 'react';
import { useEffect } from 'react';




const cartfromlocal = JSON.parse(localStorage.getItem("cart") || "[]")

export default function CustomerHome() {
    
    const [product, setProduct] = useState([
        {
          url: 'https://rukminim1.flixcart.com/image/300/300/l51d30w0/shoe/z/w/c/10-mrj1914-10-aadi-white-black-red-original-imagft9k9hydnfjp.jpeg?q=70',
          name: 'TRQ White Shoes',
          category: 'Shoes',
          seller: 'AMZ Seller Ghz',
          price: 1999,
          description: "Lorem Ipsum",
        },
        {
          url: 'https://5.imimg.com/data5/KC/PC/MY-38629861/dummy-chronograph-watch-500x500.jpg',
          name: 'LOREM Watch Black',
          category: 'Watches',
          seller: 'Watch Ltd Siyana',
          price: 2599,
          description: "Lorem Ipsum",
        },
        {
          url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq39iB_cO6uhZ59vubrqVuYTJQH-4Qa0hU9g&usqp=CAU',
          name: 'AMZ Laptop 8GB RAM',
          category: 'Laptops',
          seller: 'Delhi Laptops',
          price: 50000
        },
        {
          url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfvoDzLqrT7GwU3z7Ccp0Cl9rV0ZnU9DcmEg&usqp=CAU',
          name: 'Security Camera',
          category: 'CCTV',
          seller: 'Camron LTD',
          price: 4000,
          description: "Lorem Ipsum",
        },
        {
          url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSG9e8Axt-h9q8EIybKfjGzbkIWJAr50_BX7Q&usqp=CAU',
          name: 'Watch Pink',
          category: 'Watches',
          seller: 'Watch Ltd',
          price: 2000,
          description: "Lorem Ipsum",
        },
        {
          url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9xzgtOpMxdpfgBOg3OKsEqYRkNBbuprJj4w&usqp=CAU',
          name: 'Cup red Color',
          category: 'Cup',
          seller: 'ABS Ltd',
          price: 100,
          description: "Lorem Ipsum",
        },
      ])
      const [cart, setCart] = useState(cartfromlocal);
      const [showCart, setShowCart] = useState(false)
    
      const addToCart = async (data) => {
        setCart([...cart, { ...data, quantity: 1 }])
      }
    
      useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(cart));
      }, [cart])
    
      const handleShow = (value) => {
        setShowCart(value)
      }
    
    
    
    
    return (
        <div>
            <Navbar count={cart.length}
                handleShow={handleShow}></Navbar>
            <br />
            <div>
            <Carousel />
            <br />
            <br />
            <br />
            <br />
            <Product product={product} addToCart={addToCart} ></Product>
            <ToTopBtn />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <Pagination />
          </div>
      <Footer />



        </div>
    )
}
