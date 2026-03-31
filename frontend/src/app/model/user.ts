

export interface User {

    id?:number,
    fullname:string,
    username:string,
    phone:number,
    email:string,
    password:string,
    numTicketsBought:number,
    favoriteGenre:string,
    profileImage?:boolean
    roles?: string[]

}