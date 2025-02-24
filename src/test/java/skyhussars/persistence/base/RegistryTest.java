/*
 * Copyright (c) 2017, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package skyhussars.persistence.base;

import java.io.File;
import java.io.IOException;
import static java.util.Arrays.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class RegistryTest {
    
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File tmpFile(){
        File tmp;
        try {
            tmp = folder.newFolder();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return tmp;
    }
    
    @Test
    public void testCannotRetreiveByNullNameFromRegistry(){
        expected.expect(NullPointerException.class);
        Registry registry = new Registry(null);
        registry.item(null);
    }
    
    @Test
    public void testThatNoneExistingItemRetrievedAsNone(){
            Registry registry = new Registry(tmpFile());
            assert(registry.item("Test") == Optional.empty());
    }
    
    @Test
    public void testThatSameNameCannotBeRegisteredTwice(){
        
        Registry registry = new Registry(tmpFile());
        assert(registry.register("Test", "Test1"));
        assert(!registry.register("Test", "Test2"));
    }
    
    @Test
    public void testThatRegistryEntryCanBeRetrieved(){
        Registry<String> registry = new Registry<>(tmpFile());
        assert(registry.register("Test", "Test1"));
        assert(registry.item("Test").map(s -> s.equals("Test1")).orElse(Boolean.FALSE));
    }
    
    @Test
    public void testThatRegistryItemNamesCanBeQueried(){
        Registry<String> registry = new Registry<>(tmpFile());
        assert(registry.register("Test a", "Test1"));
        assert(registry.register("Test b", "Test2"));
        assert(registry.register("Test c", "Test2"));
        assert(registry.availableItems().containsAll(asList("Test a","Test b","Test c")));
    }
    
    @Test
    public void testThatSameDescriptorCanAppearMultipleTimes(){
        Registry<String> registry = new Registry<>(tmpFile());
        assert(registry.register("Test a", "Test1"));
        assert(registry.register("Test b", "Test1"));
        String item1 = registry.item("Test a").get();
        String item2 = registry.item("Test b").get();
        assert(item1.equals(item2));
    }
}
