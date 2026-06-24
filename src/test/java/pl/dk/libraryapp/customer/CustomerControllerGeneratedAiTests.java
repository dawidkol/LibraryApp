package pl.dk.libraryapp.customer;

        mockMvc.perform(delete("/customers/" + id))
                // THEN - Status 204 No Content (successful deletion)
                .andExpect(status().isNoContent());
