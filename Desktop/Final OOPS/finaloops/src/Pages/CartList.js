import { useEffect, useState } from 'react';
import '../App.css';



const cartfromlocal = JSON.parse(localStorage.getItem("cart") || "[]")
function CartList({ cart }) {

    const [CART, setCART] = useState(cartfromlocal)

    useEffect(() => {
        setCART(cart)
    }, [cart])

    useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(CART));
    }, [CART])

    return (
        <div className='k'>
            <h1 className='k2'>Your Cart</h1>

            {
                CART?.map((cartItem, cartindex) => {
                    return (
                        <div className='cart'>
                            <div className='row mt-3'>
                                <div className='col-2' />
                                <div className='col-2'>
                                    <img src={cartItem.url} width={120} />
                                </div>

                                <div className='col-2'>

                                    <span className='mt-5'> {cartItem.name} </span>

                                </div>

                                <div className='col-1'>

                                    <button class='btn btn-danger me-1'
                                        onClick={() => {
                                            const _CART = CART.map((item, index) => {
                                                return cartindex === index ? { ...item, quantity: item.quantity > 0 ? item.quantity - 1 : 0 } : item
                                            })
                                            setCART(_CART)
                                            // localStorage.setItem("cart", JSON.stringify(cart));
                                        }}
                                    >-</button>


                                    <span> {cartItem.quantity} </span>

                                    <button class='btn btn-success ms-1'
                                        onClick={() => {
                                            const _CART = CART.map((item, index) => {
                                                return cartindex === index ? { ...item, quantity: item.quantity + 1 } : item
                                            })
                                            setCART(_CART)
                                            // localStorage.setItem("cart", JSON.stringify(cart));

                                        }}
                                    >+</button>
                                </div>
                                <div className='col-1'>
                                    <span> Rs. {cartItem.price * cartItem.quantity} </span></div>
                            </div>
                        </div>
                    )
                })
            }

            <div className='row'>
                <div className='col-6' />
                <div className='col-1'>
                    <p className='para'> Total:</p>  </div>
                <div className='col-1'><p className='para'> Rs. {
                    CART.map(item => item.price * item.quantity).reduce((total, value) => total + value, 0)
                }
                </p></div>
            </div>

            <div className='row mt-4 mb-4'>
                <div className='col-2' />
                <div className='col-2'>
                    <a href="/">
                        <button className='btn btn-warning'>Continue Shopping</button>
                    </a>
                </div>
                <div className='col-2' />
                <div className='col-2'>
                    <div className='payment'>
                        <a href="#">
                            <button onClick={() => {

                            }} className='btn btn-primary'>Proceed to Payment</button>
                        </a>
                    </div>

                </div>
            </div>

        </div >
    )
}

export default CartList
