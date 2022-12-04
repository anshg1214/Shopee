import React, { Component } from 'react'
import "./Navbar.css"
// import {Link} from 'react-router-dom';
// import logo from '../logo.svg';
// import styled from 'styled-components';
// import {ButtonContainer} from './Button';
import { Link } from 'react-router-dom'




export default function Navbar(props) {


    return (


        <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">

            <div class="container">

                {/* <button class="btn btn-outline-light" type="submit">Button 2</button> */}



                <Link to='/'>
                    <span class="navbar-brand position-absolute start-0 ms-3 top-0 mt-2 h1 logo">Gada Electronics</span>
                </Link>


                <input type="text" id="search" class="form-control" placeholder="e.g refrigirator" />

                <button id="searchbtn" class="btn btn-primary" type="button">Search</button>






                <div class="dropdown position-absolute end-0" id="dd">
                    <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">Account</button>

                    <ul class="dropdown-menu dropdown-menu-dark">
                        <li><a class="dropdown-item" href="#">Profile</a></li>
                        <li><a class="dropdown-item" href="#">Wishlist</a></li>
                        <li><a class="dropdown-item" href="#">My Wallet</a></li>
                        <li><a class="dropdown-item" href="#">Order History</a></li>
                        <li><hr class="dropdown-divider" /></li>
                        <li><a class="dropdown-item" href="#">Sign Out</a></li>
                    </ul>



                </div>
                <Link to='/cart'>
                    <button class="btn btn-outline-warning position-absolute top-0 mt-2 end-0 me-5 ">Cart</button>
                </Link>


                {/* <button type="button" class="btn btn-dark">Account</button> */}








            </div>

        </nav>



    )
}

;

