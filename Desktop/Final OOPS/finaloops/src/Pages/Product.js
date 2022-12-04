import React, { Component } from 'react'
import CartList from './CartList'




export default function Product({product,addToCart}){

//       return(
//         <div class="container">
        


// <div class="card text-white bg-dark mb-3 " >
//   <div class="row g-0">
//     <div class="col-md-4">
//       <img src="https://source.unsplash.com/1400x600/?Iphone12" class="img-fluid rounded-start" alt="..."/>
//     </div>
//     <div class="col-md-8">
//       <div class="card-body">
//         <h4 class="card-title">IPhone 12<br/></h4>
//         <p class="card-text">Rs. 69,999<br/><br/></p>
//         <p class="card-text"><small class="text-muted">Rating: 4.7/5</small>
//         <button class="btn btn-outline-danger position-absolute end-0 translate-middle-x" onClick={() => addToCart(productItem)}>Add to Cart</button>
//         {/* <button class="btn btn-outline-success">View Description</button> */}
//         </p>
        
//       </div>
//     </div>
//   </div>
// </div>
//         </div>



//       )  



return (
  <div className='flex'>
      {
          product.map((productItem, productIndex) => {
              return (
                  // <div style={{ width: '33%' }}>
                  //     <div className='product-item'>
                  //         <img src={productItem.url} width="100%" />
                  //         <p>{productItem.name} | {productItem.category} </p>
                  //         <p> {productItem.seller} </p>
                  //         <p> Rs. {productItem.price} /-</p>
                  //         <button
                  //             onClick={() => addToCart(productItem)}
                  //         >Add To Cart</button>
                  //     </div>
                  // </div>

                  
  <div class="container">

<div class="card mb-3 " >
  <div class="row g-0">
    <div class="col-4">
      <img src={productItem.url}  width="400" height="200"   alt="..."/>
    </div>
    <div class="col-6">
      <div class="card-body">
        <h4 class="card-title">{productItem.name}<br/></h4>
        <p class="card-text">Rs. {productItem.price}<br/></p>
        <p class="card-text">
        {productItem.description}
        {/* <small class="text-muted">Rating: 4.7/5</small> */}
        
        
        </p>
        
      </div>

      </div>

      <div class='col-2'>
          
        <button class="btn btn-outline-success mt-5">Add to Wishlist</button> 

        <button class="btn btn-outline-danger position-absolute end-0 bottom-0 translate-middle-x mb-5" onClick={() => addToCart(productItem)}>Add to Cart</button>
        

        </div>
        </div>

        
    
    
  </div>


  
</div>




              )
          })
      }
  </div>
)


}