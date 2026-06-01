export interface Review {
    id?: number,
    description: string,
    rating: number,
    eventAssociatedId: number,
    userOwnerId: number,
    createdAt?: string 
    
}