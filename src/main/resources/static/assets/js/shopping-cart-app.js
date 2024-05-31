const app = angular.module("shopping-cart-app", []);
jQuery(document).ready(function($) {
	$('.bar-menu').click(function(event) {
		$(this).next('.list-child').slideToggle(300);
	});
});
app.controller("shopping-cart-ctrl", function($scope, $http) {
    $scope.voucher={
        voucherCode : localStorage.getItem('voucher') || '',
        errorMessage :"",
        estimate:0,
        voucherPrice:0,
        isValid: 1000,
        showMessage(message){
            alert(message)
            $scope.voucher.errorMessage=message
            $("#myModal").modal("hide");
        },
        getVoucher(code){
            if(code){
                $http.get(`/rest/voucher/code?code=`+code).then(resp => {
                    this.voucherPrice = resp.data.voucher_price ;
                    this.estimate = resp.data.estimate ;
                    this.saveVoucherCode()

                }).catch(error => {
                    console.log(error)
                })
            }else this.voucherPrice = 0

        },
        saveVoucherCode(){
            $http.get(`/rest/voucher/vadidate?code=`+$scope.voucher.voucherCode+"&total="+$scope.cart.amount).then(resp => {
                const isValidVoucher  =  resp.data;
                if(isValidVoucher!=1000){ // Voucher không hợp lệ
                    if(isValidVoucher==1001){
                        $scope.voucher.errorMessage = "Voucher không tồn tại."
                        this.voucherPrice = 0;
                        this.isValid = 2;
                        localStorage.setItem("voucher", "");
                    }else if(isValidVoucher==1002){
                        $scope.voucher.errorMessage = "Voucher hết hạn."
                        this.voucherPrice = 0;
                        this.isValid = 2;
                        localStorage.setItem("voucher", "")
                    }else if(isValidVoucher==1004){
                        $scope.voucher.errorMessage = "Hoá đơn chưa đủ điều kiện áp dụng Voucher."
                        $http.get(`/rest/voucher/code?code=`+this.voucherCode).then(resp => {
                            this.voucherPrice = 0 ;
                            this.estimate = resp.data.estimate ;
                            this.isValid = 2;
                            // localStorage.setItem("voucher_sell", "")
                        }).catch(error => {
                            console.log(error)
                        })
                    }
                    else{
                        $scope.voucher.errorMessage = "Voucher không hợp lệ."
                        this.voucherPrice = 0;
                        this.isValid = 2;
                        localStorage.setItem("voucher", "")
                    }
                }else{
                    if (this.voucherCode) this.isValid = 1;
                    $scope.voucher.errorMessage =  ""
                    localStorage.setItem("voucher", this.voucherCode);
                    this.getVoucher(this.voucherCode);

                }
            }).catch(error => {
                console.log(error)
            })
        },
        clearVoucher(){
            localStorage.removeItem("voucher");
        }
    }
    $scope.cart = {
        items: [],
        getitem : {} ,
        getcomment :[],
        get_orderid :0,
        prod : [] ,
        checkBeforeSaveToLocalStorage(quantityBuy,idProduct) {
           $http.get(`/rest/products/${idProduct}`).then(resp => {
               const foundItem = resp.data
               if(!quantityBuy) this.updateItemInCart(foundItem.product_id,1);
               else {
                   if(quantityBuy>foundItem.quantity){
                       this.updateItemInCart(foundItem.product_id,foundItem.quantity);
                       var json = JSON.stringify(angular.copy(this.items));
                       localStorage.setItem("cart", json);
                       alert("Sản phẩm bạn muốn mua vượt quá số lượng trong kho, số lượng khả dụng: "+foundItem.quantity )
                   }
                   else{
                       this.updateItemInCart(foundItem.product_id,quantityBuy);
                       var json = JSON.stringify(angular.copy(this.items));
                       localStorage.setItem("cart", json);
                   }
               }
            });
        },
        updateItemInCart(productId, quantityUpdate){
            var updatedArray = this.items.map(element => {
                if (element.product_id === productId) {
                    element.quantity = quantityUpdate;
                }
                return element;
            });
            this.items = updatedArray

            $scope.voucher.saveVoucherCode()
        },
        add(product_id) {
            var item = this.items.find(item => (item.product_id == product_id ));

            if (item) {
                $http.get(`/rest/products/${product_id}`).then(resp => {
                    let quantityNew = item.quantity + 1;
                    this.checkBeforeSaveToLocalStorage(quantityNew,item.product_id)
                })
                // this.saveToLocalStorage();
            } else {
                $http.get(`/rest/products/${product_id}`).then(resp => {
                    resp.data.quantity = 1;

                    this.items.push(resp.data);
                    this.saveToLocalStorage();
                    $scope.voucher.saveVoucherCode()

                })
            }
        },
        get_infoorderid(orderid){
        	this.get_orderid = orderid;
         this.saveToLocalStorage();
        },
        getinfComment(product_id){
        	   $http.get(`/rest/comments/${product_id}`).then(resp => {
                    this.getcomment = resp.data;
                    $scope.isshowcomment = false;
                    this.saveToLocalStorage();
                })
        },
        getinfoproducts(product_id){
       			 var item = this.items.find(item => (item.product_id == product_id ));
        	   $http.get(`/rest/products/${product_id}`).then(resp => {
                    this.getitem = resp.data;
                    this.saveToLocalStorage();
                })
        }
        ,
        maxquantity(product_id){
        	 $http.get(`/rest/products/${product_id}`).then(resp => {
                    this.prod = resp.data;
                })
        	return this.prod.quantity;
        }
        ,
        remove(product_id) {
            var index = this.items.findIndex(item => item.product_id == product_id);
            this.items.splice(index, 1);
            $scope.voucher.saveVoucherCode()
            this.saveToLocalStorage();
        },
        clear() {
            this.items = [];
            this.saveToLocalStorage();
            $scope.voucher.saveVoucherCode()

        },
        amt_of(item) {},
        get count() {
            return this.items
                .map(item => item.quantity)
                .reduce((total, quantity) => total += quantity, 0);
        },
        get amount() {
            return this.items
                .map(item => item.quantity*item.unit_price-((item.quantity*item.unit_price)*item.distcount/100))
                .reduce((total, quantity) => total += quantity, 0);
        },
        get amount1() {
            return this.items
                .map(item => item.quantity * item.unit_price)
                .reduce((total, quantity) => total += quantity, 0);
        },
         get amount2() {
            return this.items
                .map(item => (item.quantity * item.unit_price)-(item.quantity*item.unit_price-((item.quantity*item.unit_price)*item.distcount/100)))
                .reduce((total, quantity) => total += quantity, 0);
        },
        saveToLocalStorage() {
            var json = JSON.stringify(angular.copy(this.items));
            localStorage.setItem("cart", json);
        },
        loadFromLocalStorage() {
            var json = localStorage.getItem("cart");
            this.items = json ? JSON.parse(json) : [];
        }

    }
    $scope.cart.loadFromLocalStorage();
    $scope.voucher.getVoucher(localStorage.getItem('voucher') || '')
	   $scope.order = {
	        createDate: new Date(),

	        address: "",
	        phone : "",
	        status : 0,
	        intent: 'sale',
	        method: 'Paypal',
	        currency: 'USD',
	        description: 'Đã thanh toán',

	        price : $scope.cart.amount,
	        account: { username: $("#username").text()},
	        get orderDetails() {
	            return $scope.cart.items.map(item => {
	                return {
	                    product: item,
	                    price: item.unit_price,
	                    quantity: item.quantity
	                }
	            });
	        },
	        purchase() {
	            var order = angular.copy(this);
	            $http.post("/rest/orders", order).then(resp => {
	                alert("Đặt hàng thành công");
	                $scope.cart.clear();
//                     $scope.voucher.clearVoucher;
	                location.href = "/order/detail/" + resp.data.order_id;
	            }).catch(error => {
	                alert("Đặt hàng thất bại");
	                console.log(error)
	            })
	        },
	        purchase1() {
                if($scope.order.validatePhone($('#phone').val()) != ""){
                    alert(validatePhone($('#phone').val()));
                    return;
                }
                if($scope.order.validateAddress($('#address').val()) != ""){
                    alert(validateAddress($('#address').val()));
                    return;
                }
	            var order = angular.copy(this);
                order.price = order.price + 30000;
	            $http.post("/rest/orders?code="+$scope.voucher.voucherCode, order).then(resp => {
                    alert("Đang chuyển đến trang thanh toán");
                    $scope.cart.clear();
                    document.getElementById("form-pay").submit();
	            }).catch(error => {
	                alert("Đặt hàng thất bại");
	                console.log(error)
	            })
	        },
	        purchase2() {
	            $scope.cart.clear();
	        },
           validatePhone(text){
               if (text == null || text == undefined || text.length == 0) {
                   return "Vui lòng nhập số điện thoại";
               }
               var regex = /^(0[3|5|7|8|9])+([0-9]{8})\b$/i;
               if (text.match(regex))
                   return "";
               return "SĐT không hợp lệ, phải gồm 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09";
           },
           validateAddress(text){
               if (text == null || text == undefined || text.length == 0) {
                   return "Vui lòng nhập địa chỉ giao hàng";
               }
               return "";
           }
	    }
	       $scope.order1 = {
	        createDate: new Date(),

	        address: "",
	        phone : "",
	        status : 0,
	        intent: 'Sale',
	        method: 'Trả sau',
	        currency: 'VND',
	        description: 'Chưa thanh toán',
            card_id: null,

	        price : $scope.cart.amount,
	        account: { username: $("#username").text()},
	        get orderDetails() {
	            return $scope.cart.items.map(item => {
	                return {
	                    product: item,
	                    price: item.unit_price,
	                    quantity: item.quantity
	                }
	            });
	        },
	        purchase() {
	            var order = angular.copy(this);
	            order.price = order.price + 30000;
                $http.post("/rest/orders?code="+$scope.voucher.voucherCode, order).then(resp => {
                    alert("Đặt hàng thành công");
                    $scope.cart.clear();
                    $scope.voucher.clearVoucher();
                    location.href = "/order/detail/" + resp.data.order_id;
                }).catch(error => {
                    if(error?.data){
                        if(error.data.status==444)alert(error.data.data);
                        else {
                            var alertMessage = "";
                            const jsonArray = JSON.parse(error.data.message);
                            jsonArray.forEach(function(item) {
                                alertMessage += item + "\n";
                            });
                            alert(alertMessage)
                        }
                    }
                    else alert("Đặt hàng thất bại");
                    console.log(error)
                })
	        },
	        purchase1() {
	            var order = angular.copy(this);
	            $http.post("/rest/orders", order).then(resp => {
	            }).catch(error => {
	                alert("Đặt hàng thất bại");
	                console.log(error)
	            })
	        },
	        purchase2() {
	            $scope.cart.clear();
	        }
	    }


    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            var start = this.page * this.size;
            return $scope.cart.items.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1 * $scope.cart.items.length / this.size);
        },
        first() {
            this.page = 0;
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.last();
            }
        },
        next() {
            this.page++;
            if (this.page >= this.count) {
                this.first();
            }
        },
        last() {
            this.page = this.count - 1;
        }
    }

    $scope.facolist=[];
    $scope.formfavo = {
    	favorite_date : new Date(),
        account : {username : $("#username").text()} ,
        product : {product_id:0}
    };




    $scope.isShow = false;

    $scope.Addfavorite = function(idindex ,isShow) {
    		$scope.formfavo.product.product_id = idindex ;
            var favo = angular.copy($scope.formfavo);
            $http.post(`/rest/favorites`,favo).then(resp =>{
            	resp.data ,
                favorite_date = new Date(resp.data.favorite_date);
                $scope.facolist.push(resp.data);
                alert("Đã thêm sản phẩm vào danh sách yêu thích");

               	$scope.isShow = isShow;
            }).catch(error => {
                alert("Bạn cần đăng nhập để sử dụng chức năng này");
                console.log("Error", error);
            })
    }


    	$scope.deleteFavoriteUser = function(idindex ,isShow){
        $scope.formfavo.product.product_id = idindex ;
        $http.delete(`/rest/favorites/${$scope.formfavo.product.product_id}`+`/${$scope.formfavo.account.username}`).then(resp =>{
        alert("Đã xoá sản phẩm ra khỏi danh sách yêu thích");

        $scope.isShow = isShow;
        }).catch(error => {
            alert("Xoá sản phẩm ra khỏi danh sách yêu thích thất bại");
            console.log("Error", error);
        })
    }


    $scope.isUsername = $("#isusername").text();

    $scope.commentList = [];
    $scope.commentedit = {};
    $scope.commentform = {
    	comment_date : new Date(),
    	account : {username : $("#username").text()} ,
        product : {product_id:0} ,
        comment_Content : "" ,
    };

	$scope.isshowcomment = false ;
    $scope.isshowcomment = function(){
    	$scope.isshowcomment = false;
    }

     $scope.AddComment = function(id ) {
     		$scope.commentform.product.product_id = id ;
            var comm = angular.copy($scope.commentform);
            $http.post(`/rest/comments`,comm).then(resp =>{
            	resp.data ,
                comment_date = new Date(resp.data.comment_date);
                $scope.commentList.push(resp.data);
                $scope.commentform.comment_Content = "" ;
                $scope.cart.getinfComment(id);
            }).catch(error => {
                alert("Bạn cần đăng nhập để sử dụng chức năng này");
                console.log("Error", error);
            })
    };


    	$scope.deleteComment = function(cmt){
    	var index = $scope.cart.getcomment.findIndex(cmts => cmts.comment_id == cmt.comment_id);
         $scope.cart.getcomment.splice(index, 1);
         $scope.cart.saveToLocalStorage();
        $http.delete(`/rest/comments/${cmt.comment_id}`).then(resp =>{
        }).catch(error => {
            alert("Xoá bình luận thất bại");
            console.log("Error", error);
        })
    };

    	$scope.UpdateComment = function() {
            var comm = angular.copy($scope.commentedit);
            $http.put(`/rest/comments/${comm.comment_id}`,comm).then(resp =>{
               	var index = $scope.cart.getcomment.findIndex(cmts => cmts.comment_id == comm.comment_id);;
           		$scope.cart.getcomment[index] = comm;
                $scope.isshowcomment = false;
            }).catch(error => {
                alert("Chỉnh sửa bình luận thất bại ");
                console.log("Error", error);
            })
    };

     $scope.editcomment = function(cmt){
        $scope.commentedit = angular.copy(cmt);
        $scope.isshowcomment = true;
    }

});
