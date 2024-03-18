pub struct CircularBuffer<T> where T: Clone {
    // We fake using T here, so the compiler does not complain that
    // "parameter `T` is never used". Delete when no longer needed.
    buffer: Vec<Option<T>>,
    capacity: usize,
    head: usize,
    tail: usize,
}

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    EmptyBuffer,
    FullBuffer,
}

impl<T> CircularBuffer<T> where T: Clone {
    pub fn new(capacity: usize) -> Self {
        let mut buffer = Vec::with_capacity(capacity);
        
        for _ in 0..capacity {
            buffer.push(None);
        }


        CircularBuffer {
            buffer,
            capacity,
            head: 0,
            tail: 0,
        }
    }

    pub fn write(&mut self, _element: T) -> Result<(), Error> {
        if self.is_full() {
            return Err(Error::FullBuffer);
        }

        self.buffer[self.tail] = Some(_element.clone());
        self.tail = (self.tail + 1) % self.capacity;
        Ok(())
    }

    pub fn read(&mut self) -> Result<T, Error> {
        if self.is_empty() {
            return Err(Error::EmptyBuffer);
        }

        let element = self.buffer[self.head].take().unwrap();
        self.head = (self.head + 1) % self.capacity;
        Ok(element)
    }

    pub fn clear(&mut self) {
        for i in 0..self.capacity {
            self.buffer[i] = None;
        }
        self.head = 0;
        self.tail = 0;

    }

    pub fn overwrite(&mut self, _element: T) {
        if self.is_full() {
            self.head = (self.head + 1) % self.capacity;
        }
        
        self.buffer[self.tail] = Some(_element.clone());
        self.tail = (self.tail + 1) % self.capacity;
    }

    fn is_empty(&self) -> bool {
        self.head == self.tail && self.buffer[self.head].is_none()
    }

    fn is_full(&self) -> bool {
        self.head == self.tail && self.buffer[self.head].is_some()
    }

}
