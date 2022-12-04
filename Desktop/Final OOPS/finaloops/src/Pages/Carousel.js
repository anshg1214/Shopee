import React, { Component } from 'react'


// import {Link} from 'react-router-dom';
// import logo from '../logo.svg';
// import styled from 'styled-components';
// import {ButtonContainer} from './Button';




export default class Carousel extends Component {
    render() {
        return (
        

            // <div class="container">
            <div id="carouselExampleCaptions" class="carousel slide carousel-fade" data-bs-ride="carousel" >
            <div class="carousel-indicators">
                <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1" ></button>
                <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
                <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
            </div>
            <div class="carousel-inner">
                <div class="carousel-item active" data-bs-interval="6000">
                <img src="https://source.unsplash.com/1400x400/?discount" class="d-block w-100" alt="..."/>
                <div class="carousel-caption d-none d-md-block">
                    <h3>Sale</h3>
                    <p>Shop now and get upto 50% off*!!!</p>
                </div>
                </div>
                <div class="carousel-item" data-bs-interval="6000">
                <img src="https://source.unsplash.com/1400x400/?laptop" class="d-block w-100" alt="..."/>
                <div class="carousel-caption d-none d-md-block">
                    <h3>HP16, now available</h3>
                    <p>Gift your child a college-perfect laptop for this session.<br/>
                        Better late than never.
                    </p>
                </div>
                </div>
                <div class="carousel-item" data-bs-interval="6000">
                <img src="https://source.unsplash.com/1400x400/?electronics,shop" class="d-block w-100" alt="..."/>
                <div class="carousel-caption d-none d-md-block">
                    <h3>The best online electronics store</h3>
                    <p>Shop now from the world's biggest and most trusted <br/>online store and get additional benifits</p>
                </div>
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
            </div>
            // </div>
            
           
        )
    }
}
;
